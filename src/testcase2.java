

public class testcase2 
{
	public static void h(int f)
	{
		System.out.println("\ndifference is:" +f);
	}
	public static int f(int g)
	{
		int i = g*g;                 
		return i;	
	}
	
	public static void main(String[] args)
	{ 
		int x=3,z=5;   
		x=4;
		int y=3;		
		int k,j;
		int a,b,c,d;
		b=4;			
		c=3;				
		a=b+c;			
		d=a*b;			
	
	if(x<y)
	{ 
		b=a-c;			
		System.out.print(b);
	}
	else	
	{ 
	  c=b+c;		
	  if(x<=y)
	  { 
		  do
	      { 
			  d=a+b;            
			  j = f(b+c);          
			  
			  y++;
	      } 
		  while(x>y);	  
	  }
	 
	  else
	  { 
		  c=a*b;                   
		  h(b-c);                 
	  }	     	
	}
	h(a-b);	
	}
}