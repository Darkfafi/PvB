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
	
	public static final String MY_FINAL_STRING = "FinalString";
	
	public int MyPublicInteger = 1;
	
	protected float myProtectedFloat = 0.1f;
	
	private float _myPrivateFloat = 0.1f;
	
	public ProgrammingConventions()
	{
		
	}
	
	public void publicVoidMethod(int paramOne)
	{
		
	}
	
	// RULE: Getters are named 'get' + [variable name] and are written in one line.
	public float getMyPrivateFloat(){return _myPrivateFloat;}
	
	// RULE: Setters are named 'set' + [variable name] and are written in one line.
	public void setMyPrivateFloat(float value) {_myPrivateFloat = value;}
	
	protected void protectedVoidMethod()
	{
		privateVoidMethod();
	}
	
	private void privateVoidMethod()
	{
		
	}
	
	// Rule: All names for abstract classes start with the term 'Base'
	@SuppressWarnings("unused") // Only added to remove warnings for the conventions class. Do not use these without good reason!
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
}
