bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit            
extern scanf   ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll   
import scanf msvcrt.dll ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
	v resd 128
	fmt db "%d", 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
		mov EBX, v
		mov ECX, 128
		bucla:
			push ECX
			push EBX
			push fmt
			call [scanf]
			add esp, 2 * 4
			pop ECX
			add EBX, 4
			cmp EAX, 0
		loopnz bucla
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
