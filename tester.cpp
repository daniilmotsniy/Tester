#include <stdio.h>  
#include <stdlib.h>
#include <iostream> 
#include <cstring> 
#include <vector>
#include <string>

using namespace std; 

const int N = 100;

int main ()
{
	vector<string> answers;
	{
		FILE *T;
		char str[N];
		char *estr;
		
			T = fopen("text.txt", "r");
		
			if (T == NULL) {printf ("Error\n"); return -1;}
		
			while (1)
			   {
				  estr = fgets(str,sizeof(str),T);
		
			      if (estr == NULL)
			      {
			            break;
			      }
					puts(str);
					
					if(strstr(str, "+")){
						answers.push_back(str);
					}
					
					for(int i =0; i < N; i++){
		   				str[i] = 0;
					}
			   }
		
			if ( fclose (T) == EOF) printf ("Error \n");
		   
			for (int i = 0; i < N; i++) {
		        std::cout << answers[i] << std::endl; 
			}
			
			fclose(T);
	}
	
	FILE *T1;
	char *estr1;
	char str[N];
	
		T1 = fopen("text.txt", "r");
	
		if (T1 == NULL) {printf ("Error\n"); return -1;}
	
		while (1)
		   {
			  estr1 = fgets(str,sizeof(str),T1);
	
		      if (estr1 == NULL)
		      {
		            break;
		      }
				puts(str);
		   }
	
		if ( fclose (T1) == EOF) printf ("Error \n");



   return 0;
} 

