bits 32

global start
extern exit, printf, fun
import exit msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    sir dd 1234A678h, 12345678h, 1AC3B47Dh, 0FEDC9876h
    len dd ($-sir)/4
    bytes resb 100
    rank times 100 db 0
    len2 dd 0
    sum dd 0
    format1 db "%x ", 0
    format2 db 10, "%s", 10, 0
    format3 db "%d", 0
    
segment code use32 class=code
    start:
        mov esi, sir
        mov ecx, [len]
        jecxz finish
        back:
            lodsd
            push esi
            push ecx
            push sum
            push len2
            push rank
            push bytes
            push eax
            call fun
            add esp, 4*5
            pop ecx
            pop esi
        loop back
        
        mov ecx, [len2]
        mov esi, bytes
        
        back2:
        mov eax, 0
        lodsb
        push ecx
        push eax
        push format1
        call [printf]
        add esp, 4*2
        pop ecx
        loop back2
        
        push rank
        push format2
        call [printf]
        add esp, 4*2
        
        mov al, [sum]
        movsx eax, al
        push eax
        push format3
        call [printf]
        add esp, 4*2
        
        finish:
        push    dword 0
        call    [exit]
