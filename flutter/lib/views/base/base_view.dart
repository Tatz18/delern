import 'package:delern_flutter/flutter/user_messages.dart';
import 'package:delern_flutter/view_models/base/base_bloc.dart';
import 'package:flutter/material.dart';

class BaseView extends StatefulWidget {
  final Function onWillPop;
  final Widget appBar;
  final Widget body;
  final BaseBloc bloc;

  const BaseView(
      {@required this.onWillPop,
      @required this.appBar,
      @required this.body,
      @required this.bloc})
      : assert(onWillPop != null),
        assert(appBar != null),
        assert(body != null),
        assert(bloc != null);

  @override
  State<StatefulWidget> createState() => _BaseViewState();
}

class _BaseViewState extends State<BaseView> {
  final _scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  void initState() {
    widget.bloc.onPop.listen((_) => Navigator.pop(context));
    widget.bloc.onErrorOccurred.listen(_showUserMessage);
    super.initState();
  }

  @override
  Widget build(BuildContext context) => WillPopScope(
      onWillPop: widget.onWillPop,
      child: Scaffold(
        key: _scaffoldKey,
        appBar: widget.appBar,
        body: widget.body,
      ));

  void _showUserMessage(String message) {
    UserMessages.showMessage(_scaffoldKey.currentState, message);
  }

  @override
  void dispose() {
    widget.bloc.dispose();
    super.dispose();
  }
}
