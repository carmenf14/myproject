In folderul doc se afla scripul pt baza de date. Aceasta este o baza de date postgreSQL. 
Se creaza o baza de date postgreSQL si se ruleaza scriptul.

In proiect in pachetul connection se afla clasa ConnectionHelper unde se completeaza conecxiunile la baza de date creata anterior.

Servlet:
"/adaugarecurs" - primeste un JSON de forma {cont_id : id-ul contului curent
request de tip POST							denumire : denumirea cursului nou creata
											descriere: denumirea cursului nou creat} 
				-returneaza succes si un mesaj corespunzator daca cursul este creat cu succes
				-returneaza fail si un mesaj corespunzator daca nu s-a creat cursul
				
"/editareCont" - primeste un JSON de forma {id_cont : id-ul contului curent 
request de tip POST							status : profesor sau student
											nume: numele utilizatorului
											prenume: prenumele utilizatorului}
				-returneaza succes si un mesaj corespunzator daca contul este editat cu succes
				-returneaza fail si un mesaj corespunzator daca nu s-a editat contul
"/cursuri" 				- returneaza lista tuturor cursurilor
request de tip GET		-returneaza fail si un mesaj corespunzator in caz de eroare

"/cursuriprofesor" 		-primeste ca parametru id_profesor -> id-ul contului curent de profesor 
request de tip GET		-in caz de succes returneaza lista cursurilor create de profesorul cu id-ul trimis ca parametru
						-returneaza fail si un mesaj corespunzator in caz de eroare
						
"/cursuristudent"		-primeste ca parametru id_student -> id-ul contului curent de student
request de tip GET		-in caz de succes returneaza lista cursurilor la care este inscris studentul cu id-ul trimis ca parametru
						-returneaza fail si un mesaj corespunzator in caz de eroare
						
"/studenticurs"			-primeste ca parametru id_curs -> id-ul cursului
request de tip GET		-in caz de succes returneaza lista studentilor inscrisi la cursul cu id-ul trimis ca parametru
						-returneaza fail si un mesaj corespunzator in caz de eroare
						
"/login"				-primeste un JSON de forma {email : email-ul introdus in formularul de login
request de tip POST									parola : parola introdusa in formularul de login}
						-in caz de succes returneaza un JSON de forma {data: {id_cont : id-ul contului
																				email : email-ul contului
																				nume : nume utilizator
																				prenume : prenume utilizator
																				status : profesor sau student}
																	   succes: true
																	   message: "Logat cu succes!"}
						-returneaza fail si un mesaj corespunzator in caz de eroare
						
"/logout"				-primeste un JSON de forma {id_user: id-ul utilizatorului logat in aplicatie}
request de tip POST 	-in caz de succes returneaza un mesaj corespunzator
						-returneaza fail si un mesaj corespunzator in caz de eroare
						
Socket: 
"inscriere_curs"		-primeste un JSON de forma {id_curs : id-ul cursului la care se va inscrie studenul
													id_student: id-ul studentului care se va inscrie la curs}
						-in caz de succes returneaza un mesaj corespunzator
						-returneaza fail si un mesaj corespunzator in caz de eroare
						
"stergestudent"			-primeste un JSON de forma {id_curs : id-ul cursului la care se va sterge studenul
													id_student: id-ul studentului care se va sterge de la curs}
						-in caz de succes returneaza un mesaj corespunzator si motivul stergerii
						-returneaza fail si un mesaj corespunzator in caz de eroare