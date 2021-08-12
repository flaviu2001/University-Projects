; This program searches the string "push dword 0" in the file "seminar7-main.asm"
; and prints a message accordingly on the console. The search process is memory efficient
; because the whole content of the file is not saved into the memory.
;

bits 32
global start        

extern exit, printf, fopen, fclose, fread, fwrite               
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fread msvcrt.dll
import fwrite msvcrt.dll
import printf msvcrt.dll
                          
                          
segment data use32 class=data
    file db 'seminar7-main.asm',0
    modread db 'r',0
    handle dd 0
    substring db 'push dword 0',0
    len equ $-substring
    buffer times 128+len+1  db 0
    msgYes db 'Substring found!', 10, 0
    msgNo db 'Substring NOT found!', 10, 0
    
    
    
segment code use32 class=code
    search:
            ; This function finds if the substring from offset [ESI] appears in the
            ; string from offset [EDI] or not. Both strings should be ASCIIZ strings.
            ; Also the length of the substring is placed in EAX.
            ; The function returns 1 in EBX if the substring appears in the string or EBX=0 otherwise
            
            mov ebx, 0      ; substring not found
            cld
            
            repeat1:
                cmp byte [edi], 0
                je final
                push esi
                push edi
                mov ecx, eax
                repe cmpsb
                cmp ecx, 0
                je found
                pop edi
                inc edi
                pop esi            
                jmp repeat1
            
            found:
                mov ebx, 1
                add esp, 4*2
            
            final:
                ret
    
    
    
    start:
            ; open the file
            push dword modread
            push dword file
            call [fopen]
            add esp, 4*2
            
            cmp eax, 0
            je not_found_in_file
            mov [handle], eax
            
            mov edi, buffer
            
            
            repeat2:
                
                ; read 128 byte from the file
                push dword [handle]
                push dword 128
                push dword 1
                push dword edi
                call [fread]
                add esp, 4*4
                
                cmp eax, 0
                je close_file_not_found
                
                ; call search
                mov esi, substring
                mov eax, len
                call search
                cmp ebx, 1
                je found_in_file
    
                ; we copy the last 'len' bytes from the end of buffer into the beginning of the buffer
                mov esi, buffer+128-len
                mov edi, buffer
                mov ecx, len
                rep movsb
                jmp repeat2
                
            
            close_file_not_found:
                ; close file
                push dword [handle]
                call [fclose]
                add esp, 4*1
                jmp not_found_in_file
            
            
            found_in_file:
                ; print message
                push dword msgYes
                call [printf]
                add esp, 4
                ; close file
                push dword [handle]
                call [fclose]
                add esp, 4*1
                jmp sfarsit
            
            not_found_in_file:
                ; print message
                push dword msgNo
                call [printf]
                add esp, 4
            
            
    sfarsit:
            push dword 0
            call [exit]
            
            