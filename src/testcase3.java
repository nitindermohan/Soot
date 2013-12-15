public class testcase3
{
	public static int fun1(int g)
	{
		int i=0; 
		if(g< 2)
		  i=1;
		else if(g <4)
		  i=g/2;
		else
		{}
            return i; 			
	}
	public static  int fun2(int b)
	{
		int c=0; 
		for(int j=0;j<b;j++)
		  {
			for (int k=0;k<j;k++) 
				c+= b*b;
		  }
		  return c; 			
	}
        public static  int fun3(int d)
	{
		int e=10;
		if(d%2==0)
		{
			while(d!=0)
			{
			  e=e+1; 
			  d--;   
			}
		}
		else
		{
		   do
			{
				e=e*2;  
				d--;    
			}while(d!=0);
		}
		return e;
		
	}
	public static void main(String[] args)
	{ 
		int f=3,l=5;   
		int z=3,m,n,o,p; 
		m= fun1(z*l);
		n=fun2(f);
		o= fun3(f);
		p= n*o; 
				
	}



}