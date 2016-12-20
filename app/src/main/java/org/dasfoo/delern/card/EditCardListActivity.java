package org.dasfoo.delern.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.dasfoo.delern.R;
import org.dasfoo.delern.adapters.CardRecyclerViewAdapter;
import org.dasfoo.delern.callbacks.OnCardViewHolderClick;
import org.dasfoo.delern.models.Card;
import org.dasfoo.delern.viewholders.CardViewHolder;

public class EditCardListActivity extends AppCompatActivity implements OnCardViewHolderClick {

    public static final String LABEL = "label";
    public static final String DECK_ID = "deckId";
    private CardRecyclerViewAdapter mFirebaseAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.AdapterDataObserver mAdapterDataObserver;

    private String mLabel;
    private String mDeckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_deck_activity);
        configureToolbar();
        getInputVariables();
        this.setTitle(mLabel);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.f_add_card_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddCardsActivity(mDeckId, R.string.add_string);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        configureRecyclerView();

        mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.registerAdapterDataObserver(mAdapterDataObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
    }

    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getInputVariables() {
        Intent intent = getIntent();
        mLabel = intent.getStringExtra(LABEL);
        mDeckId = intent.getStringExtra(DECK_ID);
    }

    private void configureRecyclerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .build());
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mFirebaseAdapter = new CardRecyclerViewAdapter(Card.class, R.layout.card_text_view_for_deck,
                CardViewHolder.class, Card.fetchAllCardsForDeck(mDeckId));
        mFirebaseAdapter.setOnCardViewHolderClick(this);
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void startAddCardsActivity(String key, int label) {
        Intent intent = new Intent(this, AddEditCardActivity.class);
        intent.putExtra(AddEditCardActivity.DECK_ID, key);
        intent.putExtra(AddEditCardActivity.LABEL, label);
        startActivity(intent);
    }

    @Override
    public void onCardClick(int position) {
        showCardBeforeEdit(mFirebaseAdapter.getRef(position).getKey());
    }

    private void showCardBeforeEdit(String cardId) {
        Intent intent = new Intent(this, PreEditCardActivity.class);
        intent.putExtra(PreEditCardActivity.LABEL, mLabel);
        intent.putExtra(PreEditCardActivity.DECK_ID, mDeckId);
        intent.putExtra(PreEditCardActivity.CARD_ID, cardId);
        startActivity(intent);
    }
}
