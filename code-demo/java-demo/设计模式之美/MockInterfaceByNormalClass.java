public class MockInterfaceByNormalClass {

    // some attrs...

    // some funcs...
}

class MockInterface_v1 {

    // 这里是模拟接口，所以不能被实例化，可以想一下为啥用 protected
    // 而不用约束力更强的 private，因为这里用了 protected，所以并不完美
    // 较为完美的形式见下面的 v2 版本
    protected MockInterface_v1() {}

    public void someFunc(String[] someArgs) {
        throw new MethodUnSupportedException("You must override this function, because this is a mock interface.");
    }
}

class MockInterface_v2 {

    // 这里可以自定义一个作用在类上的注解，人为标识一下这个类不能被实例化（只知道能做到，不过还不会写）
    @CannotBeInstantiated
    public MockInterface_v2() {}

    public void someFunc(String[] someArgs) {
        throw new MethodUnSupportedException("You must override this function, because this is a mock interface.");
    }
}
