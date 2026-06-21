package com.armada.expiryapp.data.db.dao;

import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomRawQuery;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.armada.expiryapp.data.db.entity.Outlet;
import java.lang.Class;
import java.lang.Integer;
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
import kotlin.jvm.functions.Function1;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class OutletDao_Impl implements OutletDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Outlet> __insertAdapterOfOutlet;

  public OutletDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfOutlet = new EntityInsertAdapter<Outlet>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `outlets` (`id`,`outletCode`,`outletName`,`shortName`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Outlet entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getOutletCode() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getOutletCode());
        }
        if (entity.getOutletName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getOutletName());
        }
        if (entity.getShortName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getShortName());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<Outlet> outlets,
      final Continuation<? super Unit> $completion) {
    if (outlets == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfOutlet.insert(_connection, outlets);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object replaceAll(final List<Outlet> outlets,
      final Continuation<? super Unit> $completion) {
    return DBUtil.performInTransactionSuspending(__db, (_cont) -> {
      return OutletDao.DefaultImpls.replaceAll(OutletDao_Impl.this, outlets, _cont);
    }, $completion);
  }

  @Override
  public PagingSource<Integer, Outlet> searchByNameOrCode(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM outlets\n"
            + "        WHERE outletName LIKE '%' || ? || '%'\n"
            + "           OR outletCode  LIKE '%' || ? || '%'\n"
            + "        ORDER BY outletName ASC\n"
            + "    ";
    final RoomRawQuery _rawQuery = new RoomRawQuery(_sql, new Function1<SQLiteStatement, Unit>() {
      @Override
      @NonNull
      public Unit invoke(@NonNull final SQLiteStatement _stmt) {
        int _argIndex = 1;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        _argIndex = 2;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        return Unit.INSTANCE;
      }
    });
    return new LimitOffsetPagingSource<Outlet>(_rawQuery, __db, "outlets") {
      @Override
      protected Object convertRows(final RoomRawQuery limitOffsetQuery, final int itemCount,
          final Continuation<? super List<? extends Outlet>> $completion) {
        return DBUtil.performSuspending(__db, true, false, (_connection) -> {
          final SQLiteStatement _stmt = _connection.prepare(limitOffsetQuery.getSql());
          limitOffsetQuery.getBindingFunction().invoke(_stmt);
          try {
            final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
            final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
            final int _columnIndexOfShortName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "shortName");
            final List<Outlet> _result = new ArrayList<Outlet>();
            while (_stmt.step()) {
              final Outlet _item;
              final long _tmpId;
              _tmpId = _stmt.getLong(_columnIndexOfId);
              final String _tmpOutletCode;
              if (_stmt.isNull(_columnIndexOfOutletCode)) {
                _tmpOutletCode = null;
              } else {
                _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
              }
              final String _tmpOutletName;
              if (_stmt.isNull(_columnIndexOfOutletName)) {
                _tmpOutletName = null;
              } else {
                _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
              }
              final String _tmpShortName;
              if (_stmt.isNull(_columnIndexOfShortName)) {
                _tmpShortName = null;
              } else {
                _tmpShortName = _stmt.getText(_columnIndexOfShortName);
              }
              _item = new Outlet(_tmpId,_tmpOutletCode,_tmpOutletName,_tmpShortName);
              _result.add(_item);
            }
            return _result;
          } finally {
            _stmt.close();
          }
        }, $completion);
      }
    };
  }

  @Override
  public Object searchForDropdown(final String query,
      final Continuation<? super List<Outlet>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM outlets\n"
            + "        WHERE outletName LIKE '%' || ? || '%'\n"
            + "           OR outletCode  LIKE '%' || ? || '%'\n"
            + "        ORDER BY outletName ASC\n"
            + "        LIMIT 20\n"
            + "    ";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        _argIndex = 2;
        if (query == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, query);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfShortName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "shortName");
        final List<Outlet> _result = new ArrayList<Outlet>();
        while (_stmt.step()) {
          final Outlet _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpShortName;
          if (_stmt.isNull(_columnIndexOfShortName)) {
            _tmpShortName = null;
          } else {
            _tmpShortName = _stmt.getText(_columnIndexOfShortName);
          }
          _item = new Outlet(_tmpId,_tmpOutletCode,_tmpOutletName,_tmpShortName);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<Outlet>> $completion) {
    final String _sql = "SELECT * FROM outlets ORDER BY outletName ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfShortName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "shortName");
        final List<Outlet> _result = new ArrayList<Outlet>();
        while (_stmt.step()) {
          final Outlet _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpShortName;
          if (_stmt.isNull(_columnIndexOfShortName)) {
            _tmpShortName = null;
          } else {
            _tmpShortName = _stmt.getText(_columnIndexOfShortName);
          }
          _item = new Outlet(_tmpId,_tmpOutletCode,_tmpOutletName,_tmpShortName);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object findByCode(final String code, final Continuation<? super Outlet> $completion) {
    final String _sql = "SELECT * FROM outlets WHERE outletCode = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (code == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, code);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfShortName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "shortName");
        final Outlet _result;
        if (_stmt.step()) {
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpShortName;
          if (_stmt.isNull(_columnIndexOfShortName)) {
            _tmpShortName = null;
          } else {
            _tmpShortName = _stmt.getText(_columnIndexOfShortName);
          }
          _result = new Outlet(_tmpId,_tmpOutletCode,_tmpOutletName,_tmpShortName);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM outlets";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
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
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM outlets";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
