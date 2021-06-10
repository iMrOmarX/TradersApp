package sample;

public class IncompleteInputData extends Exception{
    private String message ;
    IncompleteInputData() {

        this.initCause(new Throwable("البيانات المدخلة غير صحيحة"));
    }


}
