# AIDL

AIDL传输复杂对象，Book对象中包含List&lt;Page>

## aidl文件在项目工程中的位置说明

1. AIDLService.aidl文件可以随意存放，并不必须放在manifest中指定的package的包下面
2. Book.aidl 必须要和Book.java存放在相同的包名下，原因是module对象的aidl文件编译后必须要合java文件产生联系
    如： 
     Book.java  --  /src/com/ymx/demo/module/Book.java
     Book.aidl  --  /aidl/com/ymx/demo/module/Book.java

3. Book.aidl 和 AIDLService.aidl 可以不用存放在同一个目录
