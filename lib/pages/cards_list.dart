import 'package:flutter/material.dart';

import '../flutter/localization.dart';
import '../view_models/card_view_model.dart';
import '../view_models/deck_view_model.dart';
import '../widgets/observing_grid_view.dart';

class CardsListPage extends StatefulWidget {
  final DeckViewModel _deckViewModel;

  CardsListPage(this._deckViewModel);

  @override
  _CardsListState createState() => new _CardsListState();
}

class _CardsListState extends State<CardsListPage> {
  CardsViewModel viewModel;
  bool _active = false;

  @override
  void initState() {
    viewModel = new CardsViewModel(widget._deckViewModel.key);
    super.initState();
  }

  @override
  void deactivate() {
    viewModel.deactivate();
    _active = false;
    super.deactivate();
  }

  @override
  void dispose() {
    super.dispose();
    viewModel.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (!_active) {
      viewModel.activate();
      _active = true;
    }

    return new Scaffold(
      appBar: new AppBar(title: new Text(widget._deckViewModel.name)),
      body: new ObservingGrid(
        maxCrossAxisExtent: 240.0,
        items: viewModel.cards,
        itemBuilder: (item) => new CardGridItem(item),
        numberOfCardsLabel: AppLocalizations.of(context).numberOfCards,
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: null,
        child: new Icon(Icons.add),
      ),
    );
  }
}

class CardGridItem extends StatelessWidget {
  final CardViewModel card;

  CardGridItem(this.card);

  @override
  Widget build(BuildContext context) {
    return new Card(
      color: Colors.transparent,
      child: new Material(
        color: Colors.greenAccent,
        child: new InkWell(
          splashColor: Theme.of(context).splashColor,
          onTap: () => print(card.front),
          child: new Container(
            padding: const EdgeInsets.all(5.0),
            child: new Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                new Text(
                  card.front,
                  maxLines: 3,
                  softWrap: true,
                  textAlign: TextAlign.center,
                  style: new TextStyle(
                    fontSize: 18.0,
                  ),
                ),
                new Container(
                  padding: const EdgeInsets.only(top: 10.0),
                  child: new Text(
                    card.back,
                    maxLines: 3,
                    softWrap: true,
                    textAlign: TextAlign.center,
                    style: new TextStyle(
                      fontSize: 14.0,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}