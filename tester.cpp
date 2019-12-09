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
	vector<char> answers; //array with answers
	vector<string> all;
	vector<char> input;
	{	// block that allow to find answers
		FILE *T;
		char str[N];
		char *estr;
		int i, j;
		
			T = fopen("text.txt", "r");
			if (T == NULL) {printf ("Error\n"); return -1;}
		
			for(i = 0; 1; i++)
			   {
				  estr = fgets(str,sizeof(str),T);
			      if (estr == NULL)
			      {
					break;
			      }

					int flag = strchr(str, '+') - str ; // finding number of '+'
					
					if(strstr(str, "+")){
						for(i = flag; i < N; i++){
							str[i] = 0;
						}
						answers.push_back(str[0]);
					}
					if (str[0] != '\n' && str[1] != 0) {
						all.push_back(str);
					}
			   }
			   
			cout << all.size() << endl;
			
			puts("\n***********\n");
			
				for(j = 0; j < all.size()/4; j++){
					for (i = 0; i < 4; i++) {
						if (i % 4) {
							cout << all[i] << endl;
						} else {
							cout << "Question: " << all[i] << endl;
						}
					}
					cin >> input[j];
				}
	
			puts("\n***********\n");
			
			for (i = 0; i < answers.size(); ++i) {
				cout << answers[i] << ' ';
			}
			
			if ( fclose (T) == EOF) printf ("Error \n");
	}




   return 0;
} 

