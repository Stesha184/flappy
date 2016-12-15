package cz.uhk.pro2.flappy.game;
/**
 * rozhrani pro objektu, ktere potrebuji vedet,
 * kolik casu(ticku) ubehlo od zacatku hry
 
 *
 */
public interface TickAware {
	/**
	 * zmeni stav herni entity s ohledem na zmenu herniho casu 
	 * @param tickSinceStart cas (pocet 'ticku') od zahajeni hry
	 */
	void tick(long tickSinceStart);

}
