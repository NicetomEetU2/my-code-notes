public class MockAbstractClassByNormalClass {
    
    // some attrs...

    // some funcs...
}

class MockAbstractClass {

    // some attrs...

    public MockAbstractClass() {
        // initial attrs...
    }

    public void someAbstractFunc(String[] someArgs) {
        throw new MethodUnSupportedException("You must override this function, because this class is a mock abstract class.");
    }
    
    public void someNormalFunc(String[] someArgs) {
        // do sth...
    }
}
