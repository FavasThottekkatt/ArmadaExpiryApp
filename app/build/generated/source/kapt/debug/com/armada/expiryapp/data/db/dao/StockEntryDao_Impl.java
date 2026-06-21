package com.armada.expiryapp.data.db.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.armada.expiryapp.data.db.entity.StockEntry;
import java.lang.Class;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class StockEntryDao_Impl implements StockEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<StockEntry> __insertAdapterOfStockEntry;

  public StockEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfStockEntry = new EntityInsertAdapter<StockEntry>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `stock_entries` (`id`,`barcode`,`description`,`productCode`,`isOos`,`quantity`,`unit`,`outletName`,`outletCode`,`merchandiser`,`salesman`,`entryTimestamp`,`archived`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final StockEntry entity) {
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
        final int _tmp = entity.isOos() ? 1 : 0;
        statement.bindLong(5, _tmp);
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
        final int _tmp_1 = entity.getArchived() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
      }
    };
  }

  @Override
  public Object upsert(final StockEntry entry, final Continuation<? super Long> arg1) {
    if (entry == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfStockEntry.insertAndReturnId(_connection, entry);
    }, arg1);
  }

  @Override
  public Object getActiveEntries(final String outletCode, final String merchandiser,
      final String salesman, final Continuation<? super List<StockEntry>> arg3) {
    final String _sql = "\n"
            + "        SELECT * FROM stock_entries\n"
            + "        WHERE outletCode   = ?\n"
            + "          AND merchandiser = ?\n"
            + "          AND salesman     = ?\n"
            + "          AND archived = 0\n"
            + "        ORDER BY description ASC\n"
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
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfBarcode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "barcode");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfProductCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "productCode");
        final int _columnIndexOfIsOos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isOos");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final int _columnIndexOfUnit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unit");
        final int _columnIndexOfOutletName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletName");
        final int _columnIndexOfOutletCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "outletCode");
        final int _columnIndexOfMerchandiser = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "merchandiser");
        final int _columnIndexOfSalesman = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "salesman");
        final int _columnIndexOfEntryTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "entryTimestamp");
        final int _columnIndexOfArchived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "archived");
        final List<StockEntry> _result = new ArrayList<StockEntry>();
        while (_stmt.step()) {
          final StockEntry _item;
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
          final boolean _tmpIsOos;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsOos));
          _tmpIsOos = _tmp != 0;
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
          final int _tmp_1;
          _tmp_1 = (int) (_stmt.getLong(_columnIndexOfArchived));
          _tmpArchived = _tmp_1 != 0;
          _item = new StockEntry(_tmpId,_tmpBarcode,_tmpDescription,_tmpProductCode,_tmpIsOos,_tmpQuantity,_tmpUnit,_tmpOutletName,_tmpOutletCode,_tmpMerchandiser,_tmpSalesman,_tmpEntryTimestamp,_tmpArchived);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg3);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> arg1) {
    final String _sql = "DELETE FROM stock_entries WHERE id = ?";
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
  public Object deleteAllForSession(final String outletCode, final String merchandiser,
      final String salesman, final Continuation<? super Unit> arg3) {
    final String _sql = "\n"
            + "        DELETE FROM stock_entries\n"
            + "        WHERE outletCode   = ?\n"
            + "          AND merchandiser = ?\n"
            + "          AND salesman     = ?\n"
            + "          AND archived = 0\n"
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
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg3);
  }

  @Override
  public Object archiveForOutlet(final String outletCode, final Continuation<? super Unit> arg1) {
    final String _sql = "\n"
            + "        UPDATE stock_entries SET archived = 1\n"
            + "        WHERE outletCode = ? AND archived = 0\n"
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
