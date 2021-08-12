bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    s1 dd 1, 2, 3
    len1 equ ($-s1)/4
    s2 dd 2, 3, 4, 5, 6
    len2 equ ($-s2)/4
    r times len1 + len2 db 0
    p1 dd 0 ;pointer to the shorter array
    p2 dd 0
    lenp1 db 0
    lenp2 db 0
    x dd 0
    y db 0
    ; ...

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov dword[p1], s1
        mov dword[p2], s2
        mov byte[lenp1], len1
        mov byte[lenp2], len2
        cmp byte[lenp1], len2
        jbe here
            mov dword[p1], s2
            mov dword[p2], s1
            mov byte[lenp1], len2
            mov byte[lenp2], len1
        here:
        mov esi, [p1]
        mov ebx, [p2]
        mov edi, r
        mov ecx, 0
        mov cl, [lenp1]
        back:
            mov eax, [esi]
            mov [x], eax
            cmp eax, [ebx]
            ja here2
                mov eax, [ebx]
                mov [x], eax
            here2:

            push ebx
            push ecx
            mov ecx, 32
            mov byte[y], 0
            here3:
                mov ebx, [x]
                and ebx, 1
                add [y], bl
                ror dword[x], 1
            loop here3
            pop ecx
            pop ebx   
            mov al, [y]
            stosb
            add ebx, 4
            add esi, 4
        loop back
        mov esi, [p2]
        mov eax, 0
        mov al, [lenp2]
        mov bl, 4
        mul bl
        add esi, eax
        sub esi, 4
        mov ecx, 0
        mov cl, [lenp2]
        sub cl, [lenp1]
        cmp ecx, 0
        jz finish
        here4:
            mov al, [esi]
            mov [edi], al
            inc edi
            sub esi, 4
        loop here4
        finish:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
