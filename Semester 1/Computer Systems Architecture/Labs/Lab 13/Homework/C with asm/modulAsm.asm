bits 32
global _maxy
extern printf
import printf msvcrt.dll
segment data public data use32
	format db "idk", 0
segment code public code use32
	maxy:
		push ebp
		mov ebp, esp
		push format
		call [printf]
		add esp, 4
		;[ebp+8] - nr
		;[ebp+12] - v
		mov ecx, [ebp + 8]
		jecxz final
			mov esi, [ebp+12]
			mov ebx, 0
			bucsa:
				lodsd
				cmp ebx, eax
				jge nu
					mov ebx, eax
				nu:
			loop bucsa
		final:
		mov eax, ebx
		mov esp, ebp
		pop ebp
		ret