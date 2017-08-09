package libcore.util;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class MainTest {
	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager=new ScriptEngineManager();
		ScriptEngine engine=manager.getEngineByName("javascript");
		System.out.println(engine.eval("(3*4)%2+1+(12/2)"));
		System.out.println(engine.eval("12/2"));
	}
	
}
