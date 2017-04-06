package conventions;

/**
 * This is the class which represents all the coding conventions. 
 * This includes the way of writing and order of code.
 * The names themselves are not included as convention. The way the names are written are!
 * Specific rules are noted as 'RULE: [specification]' if it needs more explanation
 * @author Author of Class
 */
final class ProgrammingConventions 
{
	public enum MyEnum
	{
		ItemOne,
		ItemTwo
	}
	
	public static final String MY_STATIC_FINAL_STRING = "StaticFinalString";
	
	public static String ThisIsMyPublicStaticString = "StaticString";
	
	public int MyPublicInteger = 1;
	
	protected static float thisIsMyProtectedStaticFloat = 0.5f;
	
	protected float myProtectedFloat = 0.1f;
	
	private static float thisIsMyPrivateStaticFloat = 0.5f;
	
	private float _myPrivateFloat = 0.1f;
	
	public ProgrammingConventions()
	{
		
	}
	
	public static void publicStaticVoidMethod(float paramOne)
	{
		
	}
	
	/**
	 * This is a summary
	 * @param paramOne represents foo
	 */
	public void publicVoidMethod(int paramOne)
	{
		
	}
	
	// RULE: Getters are named 'get' + [variable name] and are written in one line.
	public float getMyPrivateFloat(){return _myPrivateFloat;}
	
	// RULE: Setters are named 'set' + [variable name] and are written in one line.
	public void setMyPrivateFloat(float value) {_myPrivateFloat = value;}
	
	
	protected static void protectedStaticVoidMethod(float paramOne)
	{
		privateStaticVoidMethod(thisIsMyPrivateStaticFloat, 2f);	
	}
	
	protected void protectedVoidMethod()
	{
		privateVoidMethod();
	}
	
	private static void privateStaticVoidMethod(double paramOne, float paramTwo)
	{
		
	}
	
	/**
	 * Generic method summary
	 */
	@SuppressWarnings("unused") // Only added to remove warnings for the conventions class. Do not use these without good reason!
	private <T extends BaseClass> void privateGenericMethod()
	{
		
	}
	
	private void privateVoidMethod()
	{
		
	}
	
	// Rule: All names for abstract classes start with the term 'Base'
	private abstract class BaseClass implements IMyInterface
	{
		public void InterfaceMethod()
		{
			
		}
		
		protected abstract void myProtectedAbstractVoidMethod();
	}
	
	// Rule: All names for interfaces start with the letter 'I'
	private interface IMyInterface
	{
		void InterfaceMethod();
	}
	
	/* Commented for error handling purposes.
	public class MySingletonClass
	{	
		private static MySingletonClass _instance;
		
		// RULE: Always give a singleton a static getInstance(); method.
		public static MySingletonClass getInstance()
		{
			if(_instance == null)
				_instance = new MySingletonClass();
			
			return _instance;
		}
	}
	*/
}
