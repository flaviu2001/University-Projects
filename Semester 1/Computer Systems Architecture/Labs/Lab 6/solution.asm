;Problema. Se da un sir de valori numerice intregi reprezentate pe quadworduri.
;Sa se determine suma cifrelor numarului multiplilor de 8 din sirul octetilor 
;inferiori ai cuvintelor superioare ai dublucuvintelor superioare din elementele sirului de quadworduri.


;Solutie: Parcurgand sirul de quadworduri vom obtine intai numarul multiplilor de 8 din 
;sirul octetilor inferiori ai cuvintelor superioare ai dublucuvintelor superioare din elementele sirului. Apoi vom obtine 
;cifrele acestui numar prin impartiri succesive la 10 si vom calcula suma lor.

bits 32 
global start
extern exit; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll; exit is a function that ends the calling process. It is defined in msvcrt.dll 
; our data is declared here (the variables needed by our program)
segment data use32 class=data
	sir  dq  123110110abcb0h,1116adcb5a051ad2h,4120ca11d730cbb0h
	len equ ($-sir)/8;lungimea sirului (in dublucuvinte)
	opt db 8;variabila folosita pentru testarea divizibilitatii cu 8
	zece dw 10;variabila folosita pentru determinarea cifrelor unui numar prin impartiri succesive la 10
	suma dd  0;variabila in care retinem suma cifrelor 
; our code starts here
segment code use32 class=code
    start:
	mov esi, sir
	cld;parcurgem sirul de la stanga la dreapta (DF=0).    
	mov ecx, len;vom parcurge elementele sirului intr-o bucla loop cu len iteratii.
	mov ebx, 0;in registrul ebx vom retine numarul multiplilor lui 8.
	repeta:
		lodsd;in eax vom avea dublucuvantul mai putin semnificativ al quadword-ului curent din sir
		lodsd;in eax vom avea dublucuvantul cel mai semnificativ al quadword-ului curent din sir
		shr eax, 16
		mov ah, 0;ne intereseaza doar octetul mai putin semnificativ din acest cuvant (AL)
      
		div byte[opt];vedem daca al este divizibil cu 8
		cmp ah, 0;daca restul nu este 0, reluam ciclul repeta. 
				;Altfel incrementam numarul multiplilor de 8 din registrul ebx. 
		jnz nonmultiplu
		inc ebx

		nonmultiplu:
	loop repeta;daca mai sunt elemente de parcurs(ecx>0) reia ciclul.

	;mai departe, obtinem cifrele numarului ebx in baza 10 prin impartiri succesive la 10 si calculam suma acestor cifre. 

	mov eax, ebx
	mov edx, 0
    
	transf:
		div dword[zece];impartim la 10 numarul din registrul ca sa aflam ultima cifra; aceasta cifra se afla in EDX
		add dword[suma], edx;adunam cifra la suma.
		cmp eax, 0
	jz sfarsit;daca catul este 0 inseamna ca am obtinut toate cifrele si putem parasi bucla transf
				;Altfel, il pregatim pentru o noua iteratie 
	mov edx, 0				
	jmp transf;reluam bucla pentru obtinerea unei noi cifre.

sfarsit:;incheiem programul.
           
        push dword 0; push the parameter for exit onto the stack
        call [exit]; call exit to terminate the program