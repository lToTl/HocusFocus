# HocusFocus
OOP2024 rühmatöö jätk

Autor: Arno Pilvar\
Ajakulu ~25h

Juurde sai otsitud raamatukogu JIconExctractReloaded.jar,
sest Java enda meetoditega on võimalik saada ainult 16x16 piksli suurune ikoon. Leitud raamatukogu on litsensivaba
ja leiab .exe failist kõik sellesse pakitud ikoonisuurused

Suurim aeg kulus ühe probleemi lahendamisele. Nimelt on FXML failidega stseenide loomisel
vaja luua stseeni kontroller klass. Üks kontrollerklass mitme stseeni jaoks on piisav nii kaua neid
programaatiliselt muuta ei soovi. Sellisel juhul on vajalik iga stseeni jaoks oma stseeni kontroller.
Vastasel juhul on ei lähestu stseenikontrolleri atribuudid õigesti uue stseeni jaoks ja tuleb silmitsi seista NullPointerExceptioniga.