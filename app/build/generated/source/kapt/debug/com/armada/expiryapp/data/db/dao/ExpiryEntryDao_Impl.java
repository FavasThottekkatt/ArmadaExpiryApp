package com.armada.expiryapp.data.db.dao;

import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomRawQuery;
import androidx.room.coroutines.FlowUtil;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.armada.expiryapp.data.db.entity.ExpiryEntry;
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
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExpiryEntryDao_Impl implements ExpiryEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExpiryEntry> __insertAdapterOfExpiryEntry;

  private final EntityDeleteOrUpdateAdapter<ExpiryEntry> __deleteAdapterOfExpiryEntry;

  private final EntityDeleteOrUpdateAdapter<ExpiryEntry> __updateAdapterOfExpiryEntry;

  public ExpiryEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExpiryEntry = new EntityInsertAdapter<ExpiryEntry>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `expiry_entries` (`id`,`barcode`,`description`,`productCode`,`expiryDate`,`quantity`,`unit`,`outletName`,`outletCode`,`merchandiser`,`salesman`,`entryTimestamp`,`archived`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final ExpiryEntry entity) {
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
        if (entity.getExpiryDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getExpiryDate());
        }
        statement.bindLong(6, entity.getQuantity());
        if (entity.getUnit() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getUnit());
        }
        if (entity.getOutletName() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getOutletName());
        }
        if (entity.getOutletCode() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getOutletCode());
        }
        if (entity.getMerchandiser() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getMerchandiser());
        }
        if (entity.getSalesman() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getSalesman());
        }
        if (entity.getEntryTimestamp() == null) {
          statement.bindNull(12);
        } else {
          statement.bindText(12, entity.getEntryTimestamp());
        }
        final int _tmp = entity.getArchived() ? 1 : 0;
        statement.bindLong(13, _tmp);
      }
    };
    this.__deleteAdapterOfExpiryEntry = new EntityDeleteOrUpdateAdapter<ExpiryEntry>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `expiry_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final ExpiryEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExpiryEntry = new EntityDeleteOrUpdateAdapter<ExpiryEntry>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `expiry_entries` SET `id` = ?,`barcode` = ?,`description` = ?,`productCode` = ?,`expiryDate` = ?,`quantity` = ?,`unit` = ?,`outletName` = ?,`outletCode` = ?,`merchandiser` = ?,`salesman` = ?,`entryTimestamp` = ?,`archived` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final ExpiryEntry entity) {
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
        if (entity.getExpiryDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getExpiryDate());
        }
        statement.bindLong(6, entity.getQuantity());
        if (entity.getUnit() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getUnit());
        }
        if (entity.getOutletName() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getOutletName());
        }
        if (entity.getOutletCode() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getOutletCode());
        }
        if (entity.getMerchandiser() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getMerchandiser());
        }
        if (entity.getSalesman() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getSalesman());
        }
        if (entity.getEntryTimestamp() == null) {
          statement.bindNull(12);
        } else {
          statement.bindText(12, entity.getEntryTimestamp());
        }
        final int _tmp = entity.getArchived() ? 1 : 0;
        statement.bindLong(13, _tmp);
        statement.bindLong(14, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final ExpiryEntry entry, final Continuation<? super Long> arg1) {
    if (entry == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfExpiryEntry.insertAndReturnId(_connection, entry);
    }, arg1);
  }

  @Override
  public Object delete(final ExpiryEntry entry, final Continuation<? super Unit> arg1) {
    if (entry == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __deleteAdapterOfExpiryEntry.handle(_connection, entry);
      return Unit.INSTANCE;
    }, arg1);
  }

  @Override
  public Object update(final ExpiryEntry entry, final Continuation<? super Unit> arg1) {
    if (entry == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __updateAdapterOfExpiryEntry.handle(_connection, entry);
      return Unit.INSTANCE;
    }, arg1);
  }

  @Override
  public PagingSource<Integer, ExpiryEntry> getActiveEntriesPaged(final String outletCode) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "        ORDER BY expiryDate ASC\n"
            + "    ";
    final RoomRawQuery _rawQuery = new RoomRawQuery(_sql, new Function1<SQLiteStatement, Unit>() {
      @Override
      @NonNull
      public Unit invoke(@NonNull final SQLiteStatement _stmt) {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        return Unit.INSTANCE;
      }
    });
    return new LimitOffsetPagingSource<ExpiryEntry>(_rawQuery, __db, "expiry_entries") {
      @Override
      protected Object convertRows(final RoomRawQuery limitOffsetQuery, final int itemCount,
          final Continuation<? super List<? extends ExpiryEntry>> $completion) {
        return DBUtil.performSuspending(__db, true, false, (_connection) -> {
          final SQLiteStatement _stmt = _connection.prepare(limitOffsetQuery.getSql());
          limitOffsetQuery.getBindingFunction().invoke(_stmt);
          try {
            final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
            final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
            final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
            final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
            final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
            final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
            final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
            final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
            final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
            final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
            final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
            final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
            final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
            while (_stmt.step()) {
              final ExpiryEntry _item;
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
              final String _tmpExpiryDate;
              if (_stmt.isNull(_columnIndexOfExpiryDate)) {
                _tmpExpiryDate = null;
              } else {
                _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
              }
              final int _tmpQuantity;
              _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
              final String _tmpUnit;
              if (_stmt.isNull(_columnIndexOfUnit)) {
                _tmpUnit = null;
              } else {
                _tmpUnit = _stmt.getText(_columnIndexOfUnit);
              }
              final String _tmpOutletName;
              if (_stmt.isNull(_columnIndexOfOutletName)) {
                _tmpOutletName = null;
              } else {
                _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
              }
              final String _tmpOutletCode;
              if (_stmt.isNull(_columnIndexOfOutletCode)) {
                _tmpOutletCode = null;
              } else {
                _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
              }
              final String _tmpMerchandiser;
              if (_stmt.isNull(_columnIndexOfMerchandiser)) {
                _tmpMerchandiser = null;
              } else {
                _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
              }
              final String _tmpSalesman;
              if (_stmt.isNull(_columnIndexOfSalesman)) {
                _tmpSalesman = null;
              } else {
                _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
              }
              final String _tmpEntryTimestamp;
              if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
                _tmpEntryTimestamp = null;
              } else {
                _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
              }
              final boolean _tmpArchived;
              final int _tmp;
              _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
              _tmpArchived = _tmp != 0;
              _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
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
  public PagingSource<Integer, ExpiryEntry> searchActiveEntriesPaged(final String outletCode,
      final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "          AND (description LIKE '%' || ? || '%' OR productCode LIKE '%' || ? || '%')\n"
            + "        ORDER BY expiryDate ASC\n"
            + "    ";
    final RoomRawQuery _rawQuery = new RoomRawQuery(_sql, new Function1<SQLiteStatement, Unit>() {
      @Override
      @NonNull
      public Unit invoke(@NonNull final SQLiteStatement _stmt) {
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
        return Unit.INSTANCE;
      }
    });
    return new LimitOffsetPagingSource<ExpiryEntry>(_rawQuery, __db, "expiry_entries") {
      @Override
      protected Object convertRows(final RoomRawQuery limitOffsetQuery, final int itemCount,
          final Continuation<? super List<? extends ExpiryEntry>> $completion) {
        return DBUtil.performSuspending(__db, true, false, (_connection) -> {
          final SQLiteStatement _stmt = _connection.prepare(limitOffsetQuery.getSql());
          limitOffsetQuery.getBindingFunction().invoke(_stmt);
          try {
            final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
            final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
            final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
            final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
            final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
            final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
            final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
            final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
            final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
            final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
            final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
            final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
            final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
            while (_stmt.step()) {
              final ExpiryEntry _item;
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
              final String _tmpExpiryDate;
              if (_stmt.isNull(_columnIndexOfExpiryDate)) {
                _tmpExpiryDate = null;
              } else {
                _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
              }
              final int _tmpQuantity;
              _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
              final String _tmpUnit;
              if (_stmt.isNull(_columnIndexOfUnit)) {
                _tmpUnit = null;
              } else {
                _tmpUnit = _stmt.getText(_columnIndexOfUnit);
              }
              final String _tmpOutletName;
              if (_stmt.isNull(_columnIndexOfOutletName)) {
                _tmpOutletName = null;
              } else {
                _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
              }
              final String _tmpOutletCode;
              if (_stmt.isNull(_columnIndexOfOutletCode)) {
                _tmpOutletCode = null;
              } else {
                _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
              }
              final String _tmpMerchandiser;
              if (_stmt.isNull(_columnIndexOfMerchandiser)) {
                _tmpMerchandiser = null;
              } else {
                _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
              }
              final String _tmpSalesman;
              if (_stmt.isNull(_columnIndexOfSalesman)) {
                _tmpSalesman = null;
              } else {
                _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
              }
              final String _tmpEntryTimestamp;
              if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
                _tmpEntryTimestamp = null;
              } else {
                _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
              }
              final boolean _tmpArchived;
              final int _tmp;
              _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
              _tmpArchived = _tmp != 0;
              _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
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
  public Flow<Integer> getExpiredCountFlow(final String outletCode, final String today) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0 AND expiryDate < ?\n"
            + "    ";
    return FlowUtil.createFlow(__db, false, new String[] {"expiry_entries"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (today == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, today);
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
  public Flow<Integer> getWithin30CountFlow(final String outletCode, final String today,
      final String d30) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "          AND expiryDate >= ? AND expiryDate <= ?\n"
            + "    ";
    return FlowUtil.createFlow(__db, false, new String[] {"expiry_entries"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (today == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, today);
        }
        _argIndex = 3;
        if (d30 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, d30);
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
  public Flow<Integer> getWithin60CountFlow(final String outletCode, final String d30,
      final String d60) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "          AND expiryDate > ? AND expiryDate <= ?\n"
            + "    ";
    return FlowUtil.createFlow(__db, false, new String[] {"expiry_entries"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (d30 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, d30);
        }
        _argIndex = 3;
        if (d60 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, d60);
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
  public Flow<Integer> getWithin90CountFlow(final String outletCode, final String d60,
      final String d90) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "          AND expiryDate > ? AND expiryDate <= ?\n"
            + "    ";
    return FlowUtil.createFlow(__db, false, new String[] {"expiry_entries"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 2;
        if (d60 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, d60);
        }
        _argIndex = 3;
        if (d90 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, d90);
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
  public Object getExpiredCount(final String outletCode, final String today,
      final Continuation<? super Integer> arg2) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0 AND expiryDate < ?\n"
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
        if (today == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, today);
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
    }, arg2);
  }

  @Override
  public Object getWithin30Count(final String outletCode, final String today, final String d30,
      final Continuation<? super Integer> arg3) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "          AND expiryDate >= ? AND expiryDate <= ?\n"
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
        if (today == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, today);
        }
        _argIndex = 3;
        if (d30 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, d30);
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
    }, arg3);
  }

  @Override
  public Object findById(final long id, final Continuation<? super ExpiryEntry> arg1) {
    final String _sql = "SELECT * FROM expiry_entries WHERE id = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final ExpiryEntry _result;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _result = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
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
  public Object findDuplicate(final String barcode, final String outletCode,
      final String expiryDate, final String merchandiser,
      final Continuation<? super ExpiryEntry> arg4) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE barcode      = ?\n"
            + "          AND outletCode   = ?\n"
            + "          AND expiryDate   = ?\n"
            + "          AND merchandiser = ?\n"
            + "          AND archived = 0\n"
            + "        LIMIT 1\n"
            + "    ";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (barcode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, barcode);
        }
        _argIndex = 2;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        _argIndex = 3;
        if (expiryDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, expiryDate);
        }
        _argIndex = 4;
        if (merchandiser == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, merchandiser);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final ExpiryEntry _result;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _result = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg4);
  }

  @Override
  public PagingSource<Integer, ExpiryEntry> getArchivedEntriesPaged(final String outletCode) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 1\n"
            + "        ORDER BY expiryDate ASC\n"
            + "    ";
    final RoomRawQuery _rawQuery = new RoomRawQuery(_sql, new Function1<SQLiteStatement, Unit>() {
      @Override
      @NonNull
      public Unit invoke(@NonNull final SQLiteStatement _stmt) {
        int _argIndex = 1;
        if (outletCode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, outletCode);
        }
        return Unit.INSTANCE;
      }
    });
    return new LimitOffsetPagingSource<ExpiryEntry>(_rawQuery, __db, "expiry_entries") {
      @Override
      protected Object convertRows(final RoomRawQuery limitOffsetQuery, final int itemCount,
          final Continuation<? super List<? extends ExpiryEntry>> $completion) {
        return DBUtil.performSuspending(__db, true, false, (_connection) -> {
          final SQLiteStatement _stmt = _connection.prepare(limitOffsetQuery.getSql());
          limitOffsetQuery.getBindingFunction().invoke(_stmt);
          try {
            final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
            final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
            final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
            final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
            final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
            final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
            final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
            final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
            final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
            final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
            final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
            final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
            final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
            while (_stmt.step()) {
              final ExpiryEntry _item;
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
              final String _tmpExpiryDate;
              if (_stmt.isNull(_columnIndexOfExpiryDate)) {
                _tmpExpiryDate = null;
              } else {
                _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
              }
              final int _tmpQuantity;
              _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
              final String _tmpUnit;
              if (_stmt.isNull(_columnIndexOfUnit)) {
                _tmpUnit = null;
              } else {
                _tmpUnit = _stmt.getText(_columnIndexOfUnit);
              }
              final String _tmpOutletName;
              if (_stmt.isNull(_columnIndexOfOutletName)) {
                _tmpOutletName = null;
              } else {
                _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
              }
              final String _tmpOutletCode;
              if (_stmt.isNull(_columnIndexOfOutletCode)) {
                _tmpOutletCode = null;
              } else {
                _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
              }
              final String _tmpMerchandiser;
              if (_stmt.isNull(_columnIndexOfMerchandiser)) {
                _tmpMerchandiser = null;
              } else {
                _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
              }
              final String _tmpSalesman;
              if (_stmt.isNull(_columnIndexOfSalesman)) {
                _tmpSalesman = null;
              } else {
                _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
              }
              final String _tmpEntryTimestamp;
              if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
                _tmpEntryTimestamp = null;
              } else {
                _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
              }
              final boolean _tmpArchived;
              final int _tmp;
              _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
              _tmpArchived = _tmp != 0;
              _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
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
  public Object getArchivedEntries(final String outletCode,
      final Continuation<? super List<ExpiryEntry>> arg1) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE outletCode = ? AND archived = 1\n"
            + "        ORDER BY expiryDate ASC\n"
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
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
        while (_stmt.step()) {
          final ExpiryEntry _item;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Flow<Integer> getArchivedCountFlow(final String outletCode) {
    final String _sql = "SELECT COUNT(*) FROM expiry_entries WHERE outletCode = ? AND archived = 1";
    return FlowUtil.createFlow(__db, false, new String[] {"expiry_entries"}, (_connection) -> {
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
  public Object getEntriesForExport(final String outletCode, final String merchandiser,
      final String salesman, final String monthPrefix,
      final Continuation<? super List<ExpiryEntry>> arg4) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE outletCode   = ?\n"
            + "          AND merchandiser = ?\n"
            + "          AND salesman     = ?\n"
            + "          AND archived = 0\n"
            + "          AND entryTimestamp LIKE ? || '%'\n"
            + "        ORDER BY expiryDate ASC\n"
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
        if (merchandiser == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, merchandiser);
        }
        _argIndex = 3;
        if (salesman == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, salesman);
        }
        _argIndex = 4;
        if (monthPrefix == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, monthPrefix);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
        while (_stmt.step()) {
          final ExpiryEntry _item;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg4);
  }

  @Override
  public Object getAllEntriesForMerchandiserMonth(final String merchandiser, final String salesman,
      final String monthPrefix, final Continuation<? super List<ExpiryEntry>> arg3) {
    final String _sql = "\n"
            + "        SELECT * FROM expiry_entries\n"
            + "        WHERE merchandiser = ?\n"
            + "          AND salesman     = ?\n"
            + "          AND archived = 0\n"
            + "          AND entryTimestamp LIKE ? || '%'\n"
            + "        ORDER BY outletName ASC, expiryDate ASC\n"
            + "    ";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (merchandiser == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, merchandiser);
        }
        _argIndex = 2;
        if (salesman == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, salesman);
        }
        _argIndex = 3;
        if (monthPrefix == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, monthPrefix);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
        while (_stmt.step()) {
          final ExpiryEntry _item;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg3);
  }

  @Override
  public Object getLastActiveEntry(final Continuation<? super ExpiryEntry> arg0) {
    final String _sql = "SELECT * FROM expiry_entries WHERE archived = 0 ORDER BY entryTimestamp DESC LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final ExpiryEntry _result;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _result = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
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
  public Object getActiveCountForOutlet(final String outletCode,
      final Continuation<? super Integer> arg1) {
    final String _sql = "SELECT COUNT(*) FROM expiry_entries WHERE archived = 0 AND outletCode = ?";
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
  public Object getTotalActiveCount(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM expiry_entries WHERE archived = 0";
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
  public Object getRecentEntries(final int limit,
      final Continuation<? super List<ExpiryEntry>> arg1) {
    final String _sql = "SELECT * FROM expiry_entries ORDER BY entryTimestamp DESC LIMIT ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limit);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfExpiryDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "expiryDate");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final List<ExpiryEntry> _result = new ArrayList<ExpiryEntry>();
        while (_stmt.step()) {
          final ExpiryEntry _item;
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
          final String _tmpExpiryDate;
          if (_stmt.isNull(_columnIndexOfExpiryDate)) {
            _tmpExpiryDate = null;
          } else {
            _tmpExpiryDate = _stmt.getText(_columnIndexOfExpiryDate);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          final String _tmpUnit;
          if (_stmt.isNull(_columnIndexOfUnit)) {
            _tmpUnit = null;
          } else {
            _tmpUnit = _stmt.getText(_columnIndexOfUnit);
          }
          final String _tmpOutletName;
          if (_stmt.isNull(_columnIndexOfOutletName)) {
            _tmpOutletName = null;
          } else {
            _tmpOutletName = _stmt.getText(_columnIndexOfOutletName);
          }
          final String _tmpOutletCode;
          if (_stmt.isNull(_columnIndexOfOutletCode)) {
            _tmpOutletCode = null;
          } else {
            _tmpOutletCode = _stmt.getText(_columnIndexOfOutletCode);
          }
          final String _tmpMerchandiser;
          if (_stmt.isNull(_columnIndexOfMerchandiser)) {
            _tmpMerchandiser = null;
          } else {
            _tmpMerchandiser = _stmt.getText(_columnIndexOfMerchandiser);
          }
          final String _tmpSalesman;
          if (_stmt.isNull(_columnIndexOfSalesman)) {
            _tmpSalesman = null;
          } else {
            _tmpSalesman = _stmt.getText(_columnIndexOfSalesman);
          }
          final String _tmpEntryTimestamp;
          if (_stmt.isNull(_columnIndexOfEntryTimestamp)) {
            _tmpEntryTimestamp = null;
          } else {
            _tmpEntryTimestamp = _stmt.getText(_columnIndexOfEntryTimestamp);
          }
          final boolean _tmpArchived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp != 0;
          _item = new ExpiryEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpExpiryDate,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> arg1) {
    final String _sql = "DELETE FROM expiry_entries WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Object incrementQuantity(final long id, final int additionalQty,
      final Continuation<? super Unit> arg2) {
    final String _sql = "UPDATE expiry_entries SET quantity = quantity + ? WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, additionalQty);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg2);
  }

  @Override
  public Object archiveForOutletMonth(final String outletCode, final String monthPrefix,
      final Continuation<? super Unit> arg2) {
    final String _sql = "\n"
            + "        UPDATE expiry_entries SET archived = 1\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
            + "          AND entryTimestamp LIKE ? || '%'\n"
            + "    ";
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
        if (monthPrefix == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, monthPrefix);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg2);
  }

  @Override
  public Object archiveAllActive(final Continuation<? super Unit> arg0) {
    final String _sql = "UPDATE expiry_entries SET archived = 1 WHERE archived = 0";
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
