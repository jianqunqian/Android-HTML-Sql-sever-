# Android-HTML-Sql-sever-
Android提交数据给后端，HTML提交数据给后端，后端处理并操作Sql sever

前端为index.html，用jQuery通过Ajax方法传输数据给后台
Android的布局文件为oror.xml，Post方法在PostUtil.java里，Activity在Orcode1.java文件里。通过开启新的线程来把数据POST到后台，里面有很多二维码的操作，这个可以参考，需要添加zxing.jar包
后台为Handler1.ashx，接收数据并反馈信息给前端，还操作修改数据库信息
