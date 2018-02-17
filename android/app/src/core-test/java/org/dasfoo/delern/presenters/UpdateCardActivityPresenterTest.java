/*
 * Copyright (C) 2017 Katarina Sheremet
 * This file is part of Delern.
 *
 * Delern is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Delern is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with  Delern.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dasfoo.delern.presenters;

import org.dasfoo.delern.addupdatecard.UpdateCardActivityPresenter;
import org.dasfoo.delern.models.Card;
import org.dasfoo.delern.models.Deck;
import org.dasfoo.delern.models.User;
import org.dasfoo.delern.addupdatecard.IAddUpdatePresenter;
import org.dasfoo.delern.test.FirebaseServerRule;
import org.dasfoo.delern.addupdatecard.IAddEditCardView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


/**
 * Tests UpdateCardActivityPresenter.
 */
public class UpdateCardActivityPresenterTest {

    private static int TIMEOUT = 5000;

    private Deck mDeck;
    private Card mCard;

    @Rule
    public final FirebaseServerRule mFirebaseServer = new FirebaseServerRule();

    @Before
    public void setupParamPresenter() throws Exception {
        User mUser = mFirebaseServer.signIn();
        //Create user and deck for testing
        mUser.save().blockingAwait();
        mDeck = new Deck(mUser);
        mDeck.setName("test");
        mDeck.setAccepted(true);
        mDeck.create().blockingAwait();
        mCard = new Card(mDeck);
    }

    @Test
    public void updateCard() {
        mCard.setFront("to_update_front");
        mCard.setBack("to_update_back");
        mCard.save().blockingAwait();
        Card fetchedCard = mDeck.fetchChildren(mDeck.getChildReference(Card.class), Card.class)
                .firstOrError().blockingGet().get(0);
        // It is needed to inject Presenter with one mock and one real object.
        // By using @Spy it throws NullPointerException in Deck.getChildReference
        // because @Spy creates object instance of Card$$EnhancerByMockitoWithCGLIB$$5b16c521.
        // We need exactly Card.class
        IAddEditCardView iAddEditCardView = mock(IAddEditCardView.class);
        IAddUpdatePresenter presenter =
                new UpdateCardActivityPresenter(iAddEditCardView, fetchedCard);
        presenter.onAddUpdate("front", "back");
        verify(iAddEditCardView, timeout(TIMEOUT)).cardUpdated();
    }
}