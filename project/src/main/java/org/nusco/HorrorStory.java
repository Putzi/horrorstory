package org.nusco;

import static org.nusco.Challenge.*;

//Remember: don't modify this class!
public class HorrorStory {
	public void go() throws Throwable {
		score();

		Pet myDog = new Dog("Pluto");
		assertEquals("Pluto", myDog.getName());
		score();

		Pet myCat = new Cat("Felix");
		assertEquals("Felix", myCat.getName());
		score();

		assertEquals("Pluto is a nice dog", myDog.toString());
		assertEquals("Felix is a smart cat", myCat.toString());
		score();

		PetCemetery sleepInPeace = new PetCemetery();
		assertEquals(0, sleepInPeace.getSize());
		score();

		sleepInPeace.bury(myDog);
		sleepInPeace.bury(myCat);
		assertEquals(2, sleepInPeace.getSize());
		score();

		assertSame(myDog, sleepInPeace.resuscitate("Pluto"));
		assertSame(myCat, sleepInPeace.resuscitate("Felix"));
		score();

		assertEquals("Pluto is a horrible zombie dog", myDog.toString());
		assertEquals("Felix is a smelly dead cat", myCat.toString());
		score();

		myDog.eat(myCat);
		assertEquals("I can only see Felix's tail here", myCat.toString());
		score();
		
		myDog.eat(myDog);
		assertEquals("I can only see Pluto's head here", myDog.toString());
		score();
	}
}
