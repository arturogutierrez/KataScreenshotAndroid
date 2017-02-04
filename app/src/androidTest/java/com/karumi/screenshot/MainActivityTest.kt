/*
 * Copyright (C) 2017 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.screenshot

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.karumi.screenshot.di.MainComponent
import com.karumi.screenshot.di.MainModule
import com.karumi.screenshot.model.SuperHero
import com.karumi.screenshot.model.SuperHeroesRepository
import com.karumi.screenshot.ui.view.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import java.util.*

class MainActivityTest : ScreenshotTest() {

  @Rule
  @JvmField
  var daggerRule = DaggerMockRule(MainComponent::class.java, MainModule()).set { component ->
    val app = InstrumentationRegistry.getInstrumentation()
        .targetContext
        .applicationContext as SuperHeroesApplication
    app.setComponent(component)
  }

  @Rule
  @JvmField
  var activityRule = IntentsTestRule(MainActivity::class.java, true, false)

  @Mock
  internal lateinit var repository: SuperHeroesRepository

  @Test fun showsEmptyCaseIfThereAreNoSuperHeroes() {
    givenThereAreNoSuperHeroes()

    val activity = startActivity()

    compareScreenshot(activity)
  }

  private fun givenThereAreSomeSuperHeroes(numberOfSuperHeroes: Int, avengers: Boolean): List<SuperHero> {
    val superHeroes = LinkedList<SuperHero>()
    for (i in 0..numberOfSuperHeroes - 1) {
      val superHeroName = "SuperHero - " + i
      val superHeroDescription = "Description Super Hero - " + i
      val superHero = SuperHero(superHeroName, null, avengers, superHeroDescription)
      superHeroes.add(superHero)
      `when`(repository.getByName(superHeroName)).thenReturn(superHero)
    }
    `when`(repository.all).thenReturn(superHeroes)
    return superHeroes
  }

  private fun givenThereAreNoSuperHeroes() {
    `when`(repository.all).thenReturn(emptyList<SuperHero>())
  }

  private fun startActivity(): MainActivity {
    return activityRule.launchActivity(null)
  }
}