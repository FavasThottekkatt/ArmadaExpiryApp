package com.armada.expiryapp.data.db.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.armada.expiryapp.data.db.entity.OutletItemLink;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class OutletItemLinkDao_Impl implements OutletItemLinkDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<OutletItemLink> __insertAdapterOfOutletItemLink;

  public OutletItemLinkDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfOutletItemLink = new EntityInsertAdapter<OutletItemLink>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `outlet_item_links` (`id`,`outletCode`,`barcode`,`description`,`productCode`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final OutletItemLink entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getOutletCode() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getOutletCode());
        }
        if (entity.getBarcode() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getBarcode());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDescription());
        }
        if (entity.getProductCode() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getProductCode());
        }
      }
    };
  }

  @Override
  public Object insert(final OutletItemLink link, final Continuation<? super Long> arg1) {
    if (link == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfOutletItemLink.insertAndReturnId(_connection, link);
    }, arg1);
  }

  @Override
  public Object insertAll(final List<OutletItemLink> links, final Continuation<? super Unit> arg1) {
    if (links == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfOutletItemLink.insert(_connection, links);
      return Unit.INSTANCE;
    }, arg1);
  }

  @Override
  public Object getAllForOutlet(final String outletCode,
      final Continuation<? super List<OutletItemLink>> arg1) {
    final String _sql = "SELECT * FROM outlet_item_links WHERE outletCode = ? ORDER BY description ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final List<OutletItemLink> _result = new ArrayList<OutletItemLink>();
        while (_stmt.step()) {
          final OutletItemLink _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpBarcode;
          if (_stmt.isNull(_columnIndexOfBarcode)) {
            _tmpBarcode = null;
          } else {
            _tmpBarcode = _stmt.getText(_columnIndexOfBarcode);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final String _tmpProductCode;
          if (_stmt.isNull(_columnIndexOfProductCode)) {
            _tmpProductCode = null;
          } else {
            _tmpProductCode = _stmt.getText(_columnIndexOfProductCode);
          }
          _item = new OutletItemLink(_tmpId,_tmpOutletCode,_tmpBarcode,_tmpDescription,_tmpProductCode);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Object getCountForOutlet(final String outletCode,
      final Continuation<? super Integer> arg1) {
    final String _sql = "SELECT COUNT(*) FROM outlet_item_links WHERE outletCode = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Flow<Integer> getCountForOutletFlow(final String outletCode) {
    final String _sql = "SELECT COUNT(*) FROM outlet_item_links WHERE outletCode = ?";
    return FlowUtil.createFlow(__db, false, new String[] {"outlet_item_links"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object searchForDropdown(final String outletCode, final String query,
      final Continuation<? super List<OutletItemLink>> arg2) {
    final String _sql = "\n"
            + "        SELECT * FROM outlet_item_links\n"
            + "        WHERE outletCode = ?\n"
            + "          AND (description LIKE '%' || ? || '%'\n"
            + "               OR productCode  LIKE '%' || ? || '%')\n"
            + "        ORDER BY description ASC\n"
            + "        LIMIT 50\n"
            + "    ";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        _argIndex = 3;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final List<OutletItemLink> _result = new ArrayList<OutletItemLink>();
        while (_stmt.step()) {
          final OutletItemLink _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpBarcode;
          if (_stmt.isNull(_columnIndexOfBarcode)) {
            _tmpBarcode = null;
          } else {
            _tmpBarcode = _stmt.getText(_columnIndexOfBarcode);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final String _tmpProductCode;
          if (_stmt.isNull(_columnIndexOfProductCode)) {
            _tmpProductCode = null;
          } else {
            _tmpProductCode = _stmt.getText(_columnIndexOfProductCode);
          }
          _item = new OutletItemLink(_tmpId,_tmpOutletCode,_tmpBarcode,_tmpDescription,_tmpProductCode);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg2);
  }

  @Override
  public Object isLinked(final String outletCode, final String barcode,
      final Continuation<? super Boolean> arg2) {
    final String _sql = "\n"
            + "        SELECT EXISTS(\n"
            + "            SELECT 1 FROM outlet_item_links\n"
            + "            WHERE outletCode = ? AND barcode = ?\n"
            + "        )\n"
            + "    ";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (barcode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, barcode);
        }
        final Boolean _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp == null ? null : _tmp != 0;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg2);
  }

  @Override
  public Object deleteByOutletAndBarcode(final String outletCode, final String barcode,
      final Continuation<? super Unit> arg2) {
    final String _sql = "DELETE FROM outlet_item_links WHERE outletCode = ? AND barcode = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (barcode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, barcode);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg2);
  }

  @Override
  public Object deleteAllForOutlet(final String outletCode, final Continuation<? super Unit> arg1) {
    final String _sql = "DELETE FROM outlet_item_links WHERE outletCode = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
