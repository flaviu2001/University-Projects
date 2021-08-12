bits 32 ; assembling for the 32 bits architecture

; our code starts here
segment code use32 class=code
    global prime
    prime:
        cmp dword[esp+4], 1
        jnz idk
            mov eax, 0
            ret 4
        idk:
        test dword[esp+4], 1
        jnz there
            cmp dword[esp+4], 2
            jz there2
                mov eax, 0
                ret 4
            there2:
            mov eax, 1
            ret 4
        there:
        mov ebx, 1
        back:
            add ebx, 2
			cmp ebx, [esp+4]
			jae notprime
            mov edx, 0
            mov eax, [esp+4]
            div ebx
            cmp edx, 0
            jnz notprime
                mov eax, 0
                ret 4
            notprime:
            mov eax, ebx
            mul ebx
            cmp eax, [esp+4]
            jna next
                mov eax, 1
                ret 4
            next:
            jmp back