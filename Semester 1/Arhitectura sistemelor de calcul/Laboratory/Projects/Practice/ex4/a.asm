bits 32
global start
extern exit, fun, printf
import exit msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    sir db 12, 13, 10b, 7, -3, 100, 101b, 11h, 27, -1, 4
    len dd $-sir
    res times 100 db 0
    lenres dd 0
    format db "%x ", 0

segment code use32 class=code
    start:
        push lenres
        push res
        push dword[len]
        push sir
        call fun
        add esp, 4*4
        
        mov ecx, [lenres]
        jecxz final
        mov esi, res
        back:
            mov eax, 0
            lodsb
            push ecx
            push eax
            push format
            call [printf]
            add esp, 4*2
            pop ecx
        loop back
        final:
        push    dword 0
        call    [exit]
