import 'package:delern_flutter/flutter/localization.dart';
import 'package:delern_flutter/flutter/styles.dart';
import 'package:delern_flutter/flutter/user_messages.dart';
import 'package:delern_flutter/models/deck_access_model.dart';
import 'package:delern_flutter/models/deck_model.dart';
import 'package:delern_flutter/view_models/deck_view_model.dart';
import 'package:delern_flutter/views/deck_settings/deck_type_dropdown_widget.dart';
import 'package:delern_flutter/views/helpers/save_updates_dialog.dart';
import 'package:delern_flutter/views/helpers/slow_operation_widget.dart';
import 'package:flutter/material.dart';

class DeckSettings extends StatefulWidget {
  final DeckModel _deck;

  const DeckSettings(this._deck);

  @override
  State<StatefulWidget> createState() => _DeckSettingsState();
}

class _DeckSettingsState extends State<DeckSettings> {
  final _scaffoldKey = GlobalKey<ScaffoldState>();
  final TextEditingController _deckNameController = TextEditingController();
  DeckViewModel _viewModel;
  bool _isDeckChanged = false;

  @override
  void initState() {
    _deckNameController.text = widget._deck.name;
    _viewModel = DeckViewModel(widget._deck);
    super.initState();
  }

  @override
  Widget build(BuildContext context) => WillPopScope(
        onWillPop: () async {
          if (_isDeckChanged) {
            try {
              await _viewModel.save();
            } catch (e, stackTrace) {
              UserMessages.showError(
                  () => _scaffoldKey.currentState, e, stackTrace);
              return false;
            }
          }
          return true;
        },
        child: Scaffold(
            key: _scaffoldKey,
            appBar: AppBar(title: Text(_viewModel.deck.name), actions: <Widget>[
              SlowOperationWidget(
                (cb) => IconButton(
                      icon: const Icon(Icons.delete),
                      onPressed: cb(() async {
                        var locale = AppLocalizations.of(context);
                        String deleteDeckQuestion;
                        switch (_viewModel.deck.access) {
                          case AccessType.owner:
                            deleteDeckQuestion =
                                locale.deleteDeckOwnerAccessQuestion;
                            break;
                          case AccessType.write:
                          case AccessType.read:
                            deleteDeckQuestion =
                                locale.deleteDeckWriteReadAccessQuestion;
                            break;
                        }
                        var deleteDeckDialog = await showSaveUpdatesDialog(
                            context: context,
                            changesQuestion: deleteDeckQuestion,
                            yesAnswer: locale.delete,
                            noAnswer: MaterialLocalizations.of(context)
                                .cancelButtonLabel);
                        if (deleteDeckDialog) {
                          try {
                            await _viewModel.delete();
                          } catch (e, stackTrace) {
                            UserMessages.showError(
                                () => _scaffoldKey.currentState, e, stackTrace);
                            return;
                          }
                          if (mounted) {
                            Navigator.of(context).pop();
                          }
                        }
                      }),
                    ),
              )
            ]),
            body: _buildBody()),
      );

  Widget _buildBody() => Padding(
        padding: const EdgeInsets.all(8.0),
        child: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              TextField(
                maxLines: null,
                keyboardType: TextInputType.multiline,
                controller: _deckNameController,
                style: AppStyles.primaryText,
                onChanged: (text) {
                  setState(() {
                    _isDeckChanged = true;
                    _viewModel.deck.name = text;
                  });
                },
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.only(top: 24.0),
                    child: Text(
                      AppLocalizations.of(context).deckType,
                      style: AppStyles.secondaryText,
                    ),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  DeckTypeDropdownWidget(
                    value: _viewModel.deck.type,
                    valueChanged: (newDeckType) => setState(() {
                          _isDeckChanged = true;
                          _viewModel.deck.type = newDeckType;
                        }),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Text(
                    AppLocalizations.of(context).markdown,
                    style: AppStyles.secondaryText,
                  ),
                  Switch(
                    value: _viewModel.deck.markdown,
                    onChanged: (newValue) {
                      setState(() {
                        _isDeckChanged = true;
                        _viewModel.deck.markdown = newValue;
                      });
                    },
                  )
                ],
              ),
            ],
          ),
        ),
      );
}
