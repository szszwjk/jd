package com.taotao.cart.controller;

import com.taotao.cart.jedis.JedisClient;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${CART_EXPIRE}")
    private int CART_EXPIRE;
    @Value("${TT_CART}")
    private String TT_CART;
    @Autowired
    private ItemService itemService;
    @Autowired
    private JedisClient jedisClient;
    @RequestMapping("/cart/add/{itemId}")
    public String addCartItem(@PathVariable long itemId, int num, HttpServletRequest request, HttpServletResponse response)

    {
        List<TbItem> lists=new ArrayList<>();
        boolean flag=false;
        //获取token
        String _ticket = CookieUtils.getCookieValue(request, "COOKIE_TOKEN_KEY", true);
        //判断是否登录
        if(StringUtils.isNotBlank(_ticket))
        {
            String userId = getUserId(_ticket)+"";
            //获取服务器的购物车
            String json = jedisClient.get(TT_CART+":"+userId);
            if(StringUtils.isNotBlank(json))
            {
                //相同添加到购物车中
                lists = JsonUtils.jsonToList(json,TbItem.class);
                for (int i=0;i<lists.size();i++){
                    if(lists.get(i).getId().equals(itemId))
                    {
                        lists.get(i).setNum(lists.get(i).getNum()+num);
                        flag=true;
                        break;
                    }
                }
            }
            //新物品添加
            if(!flag)
            {
                TbItem item = itemService.getItemById(itemId);
                item.setNum(num);
                String images=item.getImage();
                if(StringUtils.isNotBlank(images))
                {
                    String image = images.split(",")[0];
                    item.setImage(image);


                }
                lists.add(item);
            }
            //更新购物车
                jedisClient.set(TT_CART+":"+userId,JsonUtils.objectToJson(lists));

        }
        //没有登录的时候
        else{
            List<TbItem> list = getCartList(request);

            for (TbItem tbItem:list) {
                if(tbItem.getId()==itemId)
                {   //注意 在数据库中num代表的是库存 这里我们保存数量
                    tbItem.setNum(tbItem.getNum()+num);
                    flag=true;
                    break;
                }
            }
            if(!flag){
                TbItem item = itemService.getItemById(itemId);
                item.setNum(num);
                String images=item.getImage();
                if(StringUtils.isNotBlank(images))
                {
                    String image = images.split(",")[0];
                    item.setImage(image);


                }
                list.add(item);
            }
            //添加到cookies中
            CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(list),CART_EXPIRE,true);
        }



        return "cartSuccess";


    }
    @RequestMapping("/cart/cart")
    public String showCarList(HttpServletRequest request,HttpServletResponse response)
    {   List<TbItem> cartList = getCartList(request);
        String _ticket = CookieUtils.getCookieValue(request, "COOKIE_TOKEN_KEY", true);
        //获取token
        if(StringUtils.isNotBlank(_ticket))
       {

           String id = getUserId(_ticket)+"";

           String json = jedisClient.get(TT_CART+":"+id);
            //如果redis里的购物车不为空
           if(StringUtils.isNotBlank(json))
           {
               List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
               //遍历两集合 相同物品添加
               for(int j=0;j<cartList.size();j++)
               {
                    for(int i=0;i<tbItems.size();i++)
                    {
                        if(cartList.get(j).getId().equals(tbItems.get(i).getId()))
                        {
                            cartList.get(j).setNum(cartList.get(j).getNum()+tbItems.get(i).getNum());
                            tbItems.remove(i);
                        }
                    }
               }
               //添加新物品
              if(tbItems!=null)
              {
                  cartList.addAll(tbItems);
              }
              //清空cookies
              CookieUtils.deleteCookie(request,response,TT_CART);
           }
           //保存到redis
           jedisClient.set(TT_CART+":"+id,JsonUtils.objectToJson(cartList));

       }
        request.setAttribute("cartList",cartList);
        return "cart";
    }

    //"/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".html"
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable Long itemId, @PathVariable Integer num,
                                  HttpServletRequest request, HttpServletResponse response) {
        // 1、接收两个参数
        // 2、从cookie中取商品列表
        List<TbItem> cartList = getCartList(request);
        // 3、遍历商品列表找到对应商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 4、更新商品数量
                tbItem.setNum(num);
            }
        }
        // 5、把商品列表写入cookie。
        CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
        // 6、响应TaoTaoResult。Json数据。
        return TaotaoResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> list = getCartList(request);
        for (int i = 0;i<list.size();i++) {
            TbItem tbItem = list.get(i);
            if(tbItem.getId()==itemId.longValue()){
                list.remove(tbItem);
                break;
            }
        }
        //集合里面一定有值了 并且把他存入我们的cookie里面
        CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(list), CART_EXPIRE, true);
        return "cart";
    }
    private List<TbItem> getCartList(HttpServletRequest request)
    {
        String json = CookieUtils.getCookieValue(request, TT_CART, true);
        if(StringUtils.isBlank(json))
        {
            return new ArrayList<TbItem>();


        }
        List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
        return tbItems;
    }
    private long getUserId(String token)
    {
        String json = jedisClient.get(USER_INFO + ":" + token);
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return user.getId();
    }

}
