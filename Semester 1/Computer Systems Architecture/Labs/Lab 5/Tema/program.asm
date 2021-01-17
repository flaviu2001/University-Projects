bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a dw 1010101001010101b
    b db 11001010b
    c dd 0 ;11001010101010100101010101111001
segment code use32 class=code
        start:
        ; ...
        mov eax, 0
        mov ax, [a]
        and ax, 960 ;2^6+2^7+2^8+2^9
        shr ax, 6
        or [c], eax
        or dword[c], 48
        mov ah, 0
        mov al, [b]
        and al, 6
        shl al, 5
        or [c], eax
        ror dword[c], 8
        mov bx, [a]
        or [c], bx
        rol dword[c], 8
        ror dword[c], 24
        mov bl, [b]
        or [c], bl
        rol dword[c], 24
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program

        