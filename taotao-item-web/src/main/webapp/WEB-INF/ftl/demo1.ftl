<html>
<head>

    <title>

    </title>
</head>

    <table>
        <tr>
            <td>姓名</td><td>年龄</td><td>学号</td>
        </tr>
        <#list students as student>
        <#if student_index%2==0>
        <tr style="background-color: #2A8CFA">
        <#else >
        <tr style="background-color: red">
        </#if>

                <td>${student.name}</td><td>${student.age}</td><td>${student.num}</td>
            </tr>
        </#list>

    </table>
</html>