bits 32

global fun

segment code use32 class=code
    fun: ;void fun(int *sir, int len, int *res, int *reslen)
        push ebp
        mov ebp, esp
        
        sub esp, 4*3
        ;[esp] - mx
        ;[esp+4] - ind
        ;[esp+8] - k
        mov dword[esp], 0
        mov dword[esp+4], 0
        mov dword[esp+8], 0
        
        mov ecx, [ebp+20]
        mov dword[ecx], 0
        
        mov ecx, [ebp+12] ;len
        mov esi, [ebp+8]
        jecxz final
        back:
            mov eax, 0
            lodsb
            
            test eax, 1
            jz _even
            
            inc dword[esp+8]
            mov eax, [esp+8]
            cmp eax, [esp]
            jna after
            
            mov [esp], eax
            mov eax, esi
            sub eax, [ebp+8]
            sub eax, [esp+8]
            mov [esp+4], eax
            after:
            jmp after2
            _even:
            mov dword[esp+8], 0
            after2:
        loop back
        
        mov ecx, [esp]
        jecxz final
        mov esi, [ebp+8]
        add esi, [esp+4]
        mov edi, [ebp+16]
        back2:
            lodsb
            stosb
            mov eax, [ebp+20]
            inc dword[eax]
        loop back2
        final:
        add esp, 4*3
        pop ebp
        ret
                