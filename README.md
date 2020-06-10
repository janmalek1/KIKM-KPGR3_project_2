JAN MÁLEK: IMPLEMENTACE 3 RŮZNÝCH VARIANT DITHERINGU:
/*___________________________________________________________*/
/*___________________________________________________________*/


Aplikace vykresluje v rámci jedné scény najednou 4 varianty zobrazení původního obrázku z textury (tj. původní obrázek + 3 varianty ditheringu, viz. popis níže). 
Obrázky jsou celkem 4 a jsou volány postupně, tj. celkem uvidíme 16 různých variant obrázků (4 obrázky x 4 varianty zobrazení).

Pravděpodobně nebude úplně jasné ovládání aplikace. Proto věnujte pozornost následujícímu popisu jednotlivých variant obrázku a návodu, jak přenastavovat řídící koeficienty zobrazení.
Po stisku dané klávesy by se měla vypsat hláška o provedené změně do konzole aplikace.

/*___________________________________________________________*/

0.PŮVODNÍ OBRÁZEK (LEVÝ HORNÍ ROH):
	->obrázek zde vidíme tak, jak je uložený ve složce s texturami
	OVLÁDÁNÍ:	
		-žádné

1.NAIVNÍ (=NAIVE) DITHERING (PRAVÝ HORNÍ ROH):
	->každá jednotlivá složka RGB je porovnána oproti fixní hodnotě (thresholdNaive = 0.5)
	->defaultní threshold (thresholdNaive) o hodnotě 0.5 je možné zvýšit/snížit
	OVLÁDÁNÍ:
		-klávesa Q: zvýšení thresholdu o 0.01, způsobí ztmavení obrázku
		-klávesa A:	snížení thresholdu o 0.01, způsobí zesvětlení obrázku
		
2.NÁHODNÝ (=RANDOM) DITHERING (LEVÝ DOLNÍ ROH):
	->každá jednotlivá složka RGB je porovnána oproti náhodné hodnotě vygenerované pro daný bod
	->takto vygenerované náhodné hodnoty je možné fixně zvýšit/snížit o koeficient posunu (thresholdRandom)
	OVLÁDÁNÍ:
		-klávesa W: zvýší koeficient posunu (thresholdRandom) o 0.05, způsobí ztmavení obrázku
		-klávesa S:	sníží koeficient posunu (thresholdRandom) o 0.05, způsobí zesvětlení obrázku
		
3.MATICOVÝ (=ORDERED) DITHERING (PRAVÝ DOLNÍ ROH):
	->každá jednotlivá složka RGB je porovnána oproti hodnotě určené pozicí v matici pro daný bod
	->je možné změnit velikost matice (4x4 vs 8x8). Větší velikost matice by měla způsobit jemnější zachycení vzorů v obrázku
	->jednotlivým pozicím v matici odpovídají čtverce bodů v obrázku. Velikost těchto čtverců je možné změnit a přizpůsobit tak granularitu mřížek ve výsledném zobrazení
	OVLÁDÁNÍ:
		-klávesa BACKSPACE: umožňuje volbu mezi velikostí matice 4x4 a 8x8
		-klávesa E: zvýší škálovací koeficient (scale) pro nastavení granularity mřížek viditelných v zobrazení o 0.01
		-klávesa D: sníží škálovací koeficient (scale) pro nastavení granularity mřížek viditelných v zobrazení o 0.01