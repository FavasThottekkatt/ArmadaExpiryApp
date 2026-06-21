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
import com.armada.expiryapp.data.db.entity.Item;
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
public final class ItemDao_Impl implements ItemDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Item> __insertAdapterOfItem;

  public ItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfItem = new EntityInsertAdapter<Item>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `items` (`id`,`barcode`,`description`,`productCode`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Item entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getBarcode() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getBarcode());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescription());
        }
        if (entity.getProductCode() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getProductCode());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<Item> items, final Continuation<? super Unit> arg1) {
    if (items == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfItem.insert(_connection, items);
      return Unit.INSTANCE;
    }, arg1);
  }

  @Override
  public Object replaceAll(final List<Item> items, final Continuation<? super Unit> arg1) {
    return DBUtil.performInTransactionSuspending(__db, (_cont) -> {
      return ItemDao.DefaultImpls.replaceAll(ItemDao_Impl.this, items, _cont);
    }, arg1);
  }

  @Override
  public Object findByBarcode(final String barcode, final Continuation<? super Item> arg1) {
    final String _sql = "SELECT * FROM items WHERE barcode = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (barcode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, barcode);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final Item _result;
        if (_stmt.step()) {
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
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
          _result = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
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
  public Object findByProductCode(final String code, final Continuation<? super Item> arg1) {
    final String _sql = "SELECT * FROM items WHERE productCode = ? LIMIT 1";
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
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final Item _result;
        if (_stmt.step()) {
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
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
          _result = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
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
  public PagingSource<Integer, Item> getAllPaged() {
    final String _sql = "SELECT * FROM items ORDER BY description ASC";
    final RoomRawQuery _rawQuery = new RoomRawQuery(_sql);
    return new LimitOffsetPagingSource<Item>(_rawQuery, __db, "items") {
      @Override
      protected Object convertRows(final RoomRawQuery limitOffsetQuery, final int itemCount,
          final Continuation<? super List<? extends Item>> $completion) {
        return DBUtil.performSuspending(__db, true, false, (_connection) -> {
          final SQLiteStatement _stmt = _connection.prepare(limitOffsetQuery.getSql());
          limitOffsetQuery.getBindingFunction().invoke(_stmt);
          try {
            final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
            final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
            final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
            final List<Item> _result = new ArrayList<Item>();
            while (_stmt.step()) {
              final Item _item;
              final long _tmpId;
              _tmpId = _stmt.getLong(_columnIndexOfId);
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
              _item = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
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
  public PagingSource<Integer, Item> searchByDescriptionOrCode(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM items\n"
            + "        WHERE description LIKE '%' || ? || '%'\n"
            + "           OR productCode  LIKE '%' || ? || '%'\n"
            + "        ORDER BY description ASC\n"
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
    return new LimitOffsetPagingSource<Item>(_rawQuery, __db, "items") {
      @Override
      protected Object convertRows(final RoomRawQuery limitOffsetQuery, final int itemCount,
          final Continuation<? super List<? extends Item>> $completion) {
        return DBUtil.performSuspending(__db, true, false, (_connection) -> {
          final SQLiteStatement _stmt = _connection.prepare(limitOffsetQuery.getSql());
          limitOffsetQuery.getBindingFunction().invoke(_stmt);
          try {
            final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
            final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
            final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
            final List<Item> _result = new ArrayList<Item>();
            while (_stmt.step()) {
              final Item _item;
              final long _tmpId;
              _tmpId = _stmt.getLong(_columnIndexOfId);
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
              _item = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
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
  public Object searchForDropdown(final String query, final Continuation<? super List<Item>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM items\n"
            + "        WHERE description LIKE '%' || ? || '%'\n"
            + "           OR productCode  LIKE '%' || ? || '%'\n"
            + "        ORDER BY description ASC\n"
            + "        LIMIT 50\n"
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
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final List<Item> _result = new ArrayList<Item>();
        while (_stmt.step()) {
          final Item _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
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
          _item = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Object getAll(final Continuation<? super List<Item>> arg0) {
    final String _sql = "SELECT * FROM items ORDER BY description ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final List<Item> _result = new ArrayList<Item>();
        while (_stmt.step()) {
          final Item _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
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
          _item = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg0);
  }

  @Override
  public Object searchAll(final String query, final Continuation<? super List<Item>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM items\n"
            + "        WHERE description LIKE '%' || ? || '%'\n"
            + "           OR productCode  LIKE '%' || ? || '%'\n"
            + "        ORDER BY description ASC\n"
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
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final List<Item> _result = new ArrayList<Item>();
        while (_stmt.step()) {
          final Item _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
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
          _item = new Item(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Object getCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM items";
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
    }, arg0);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> arg0) {
    final String _sql = "DELETE FROM items";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
