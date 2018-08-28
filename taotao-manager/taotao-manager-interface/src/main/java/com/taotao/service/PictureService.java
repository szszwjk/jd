package com.taotao.service;

import com.taotao.common.pojo.PictureResult;

public interface PictureService {
	public PictureResult uploadFile(byte[] bytes, String name);
}
