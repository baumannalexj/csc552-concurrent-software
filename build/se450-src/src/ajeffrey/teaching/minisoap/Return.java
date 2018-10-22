package ajeffrey.teaching.minisoap;

public class Return implements Task {

    public String resultId;
    public Object returnResult;

    public Return () { }

    public Return (String resultId, Object returnResult) {
	this.resultId = resultId;
	this.returnResult = returnResult;
    }

    public void run (Executor exec) {
	Result result = (Result)(Result.lookup.get (resultId));
	result.returnResult (returnResult);
	Result.lookup.remove (resultId);
    }

    public String toString () { return "return " + returnResult + " to " + resultId; }


}
