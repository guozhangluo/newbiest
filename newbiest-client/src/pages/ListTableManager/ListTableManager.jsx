import React, { Component } from 'react';
import EntityListTable from '../../components/Table/EntityListTable';
import WrappedAdvancedQueryForm from '../../components/Form/QueryForm';

import {Form} from 'antd';

export default class ListTableManager extends Component {
  static displayName = 'ListTableManager';

  constructor(props) {
    super(props);
    this.state = {
      tableRrn : this.props.match.params.tableRrn
    };
  }

  render() {
    return (
      <div className="list-table-manager-page">
        <WrappedAdvancedQueryForm tableRrn={this.state.tableRrn} />
        <EntityListTable tableRrn={this.state.tableRrn} check/>
      </div>
    );
  }
}
