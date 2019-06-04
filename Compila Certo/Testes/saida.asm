sseg SEGMENT STACK ;início seg. pilha 
byte 4000h DUP(?)  ;dimensiona pilha
sseg ENDS ;fim seg. pilha
dseg SEGMENT PUBLIC ;início seg. dados
byte 4000h DUP(?) ;temporários
mov al, 20
mov DS: [0] , al
byte 20 DUP(?) ; vetor luigi em 16384
byte 0x5f ; caractere 0x5f
mov al, 20
mov DS: [2] , al
byte 20 DUP(?) ; vetor salaminho em 16405
byte 65 ; caractere 65
sword 1 ; inteiro 1
sword 4 ; inteiro 4
mov al, 20
mov DS: [4] , al
byte 20 DUP(?) ; vetor teste em 16430
sword 1 ; inteiro 1
sword 3 ; inteiro 3
sword 5 ; inteiro 5
byte 67 ; caractere 67
dseg ends  ; fim seg.dados
mov al,tails e sonic$
mov DS: [6] , al
mov al, 1
mov DS: [21] , al
mov al, 10
mov DS: [23] , al
mov al, 68
mov DS: [25] , al
mov al, 65
mov DS: [26] , al
mov al, 66
mov DS: [27] , al
mov al, 1
mov DS: [28] , al
mov al, 1
mov DS: [30] , al
mov al, 65
mov DS: [32] , al
mov al, 66
mov DS: [33] , al
mov al, 1
mov DS: [34] , al
mov al, 65
mov DS: [36] , al
mov al, 66
mov DS: [37] , al
mov al, 1
mov DS: [38] , al
mov al, 65
mov DS: [40] , al
mov al, 66
mov DS: [41] , al
mov al, 1
mov DS: [42] , al
mov al, 65
mov DS: [44] , al
mov al, 66
mov DS: [45] , al
mov al, 1
mov DS: [46] , al
mov al, 10
mov DS: [48] , al
mov al, 12
mov DS: [50] , al
mov al, 2
mov DS: [52] , al
mov al, 1
mov DS: [54] , al
mov al, 77
mov DS: [56] , al
mov al, 2
mov DS: [57] , al
mov al, 0x41
mov DS: [59] , al
mov al, 3
mov DS: [60] , al
mov al, 0x43
mov DS: [62] , al
mov al, 4
mov DS: [63] , al
mov al, 0x4f
mov DS: [65] , al
mov al, 5
mov DS: [66] , al
mov al, 78
mov DS: [68] , al
mov al, 6
mov DS: [69] , al
mov al, 72
mov DS: [71] , al
mov al, 7
mov DS: [72] , al
mov al, 0x41
mov DS: [74] , al
mov al, 8
mov DS: [75] , al
mov al, 46
mov DS: [77] , al
mov al, 1
mov DS: [78] , al
mov al, +5
mov DS: [80] , al
mov al, 2
mov DS: [82] , al
mov al, -4
mov DS: [84] , al
mov al, 3
mov DS: [86] , al
mov al, 5
mov DS: [88] , al
mov al, 3
mov DS: [90] , al
mov al, 4
mov DS: [92] , al
mov al, 4
mov DS: [94] , al
mov al, 2
mov DS: [96] , al
mov al, 5
mov DS: [98] , al
mov al, +4
mov DS: [100] , al
mov al, -2
mov DS: [102] , al
mov al, 6
mov DS: [104] , al
mov al, 10
mov DS: [106] , al
mov al, 68
mov DS: [108] , al
mov al,DS: [109]
not al
mov DS: [109] , al
mov al, 1
mov DS: [110] , al
mov al, +5
mov DS: [112] , al
mov al, 1
mov DS: [114] , al
mov al, 1
mov DS: [116] , al
mov al, +5
mov DS: [118] , al
mov al, 1
mov DS: [120] , al
mov al, 10
mov DS: [122] , al
