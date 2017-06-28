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

package org.dasfoo.delern.di.modules;

import org.dasfoo.delern.presenters.LearningCardsActivityPresenter;
import org.dasfoo.delern.views.ILearningCardsView;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 class that says how to inject LearningCardsActivity.
 */
@Module
public class LearningCardsActivityModule {
    private final ILearningCardsView mView;

    /**
     * Constructor. It gets interface as parameter that implemented in LearningCardsActivity
     * for callbacks from Presenter.
     *
     * @param view interface to init Presenter for callbacks.
     */
    public LearningCardsActivityModule(final ILearningCardsView view) {
        this.mView = view;
    }

    @Provides
    /* default */ LearningCardsActivityPresenter providePresenter() {
        return new LearningCardsActivityPresenter(mView);
    }
}
