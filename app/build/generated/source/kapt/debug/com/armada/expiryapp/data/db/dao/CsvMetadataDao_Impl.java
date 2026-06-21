package com.armada.expiryapp.data.db.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.armada.expiryapp.data.db.entity.CsvMetadata;
import java.lang.Class;
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
public final class CsvMetadataDao_Impl implements CsvMetadataDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<CsvMetadata> __insertAdapterOfCsvMetadata;

  public CsvMetadataDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCsvMetadata = new EntityInsertAdapter<CsvMetadata>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `csv_metadata` (`fileType`,`importedAt`,`recordCount`,`skippedRows`,`fileHash`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final CsvMetadata entity) {
        if (entity.getFileType() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getFileType());
        }
        if (entity.getImportedAt() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getImportedAt());
        }
        statement.bindLong(3, entity.getRecordCount());
        statement.bindLong(4, entity.getSkippedRows());
        if (entity.getFileHash() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getFileHash());
        }
      }
    };
  }

  @Override
  public Object upsert(final CsvMetadata metadata, final Continuation<? super Unit> $completion) {
    if (metadata == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfCsvMetadata.insert(_connection, metadata);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getByType(final String fileType,
      final Continuation<? super CsvMetadata> $completion) {
    final String _sql = "SELECT * FROM csv_metadata WHERE fileType = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (fileType == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, fileType);
        }
        final int _columnIndexOfFileType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fileType");
        final int _columnIndexOfImportedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importedAt");
        final int _columnIndexOfRecordCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "recordCount");
        final int _columnIndexOfSkippedRows = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skippedRows");
        final int _columnIndexOfFileHash = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fileHash");
        final CsvMetadata _result;
        if (_stmt.step()) {
          final String _tmpFileType;
          if (_stmt.isNull(_columnIndexOfFileType)) {
            _tmpFileType = null;
          } else {
            _tmpFileType = _stmt.getText(_columnIndexOfFileType);
          }
          final String _tmpImportedAt;
          if (_stmt.isNull(_columnIndexOfImportedAt)) {
            _tmpImportedAt = null;
          } else {
            _tmpImportedAt = _stmt.getText(_columnIndexOfImportedAt);
          }
          final int _tmpRecordCount;
          _tmpRecordCount = (int) (_stmt.getLong(_columnIndexOfRecordCount));
          final int _tmpSkippedRows;
          _tmpSkippedRows = (int) (_stmt.getLong(_columnIndexOfSkippedRows));
          final String _tmpFileHash;
          if (_stmt.isNull(_columnIndexOfFileHash)) {
            _tmpFileHash = null;
          } else {
            _tmpFileHash = _stmt.getText(_columnIndexOfFileHash);
          }
          _result = new CsvMetadata(_tmpFileType,_tmpImportedAt,_tmpRecordCount,_tmpSkippedRows,_tmpFileHash);
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
  public Object getAll(final Continuation<? super List<CsvMetadata>> $completion) {
    final String _sql = "SELECT * FROM csv_metadata";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfFileType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fileType");
        final int _columnIndexOfImportedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importedAt");
        final int _columnIndexOfRecordCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "recordCount");
        final int _columnIndexOfSkippedRows = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skippedRows");
        final int _columnIndexOfFileHash = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fileHash");
        final List<CsvMetadata> _result = new ArrayList<CsvMetadata>();
        while (_stmt.step()) {
          final CsvMetadata _item;
          final String _tmpFileType;
          if (_stmt.isNull(_columnIndexOfFileType)) {
            _tmpFileType = null;
          } else {
            _tmpFileType = _stmt.getText(_columnIndexOfFileType);
          }
          final String _tmpImportedAt;
          if (_stmt.isNull(_columnIndexOfImportedAt)) {
            _tmpImportedAt = null;
          } else {
            _tmpImportedAt = _stmt.getText(_columnIndexOfImportedAt);
          }
          final int _tmpRecordCount;
          _tmpRecordCount = (int) (_stmt.getLong(_columnIndexOfRecordCount));
          final int _tmpSkippedRows;
          _tmpSkippedRows = (int) (_stmt.getLong(_columnIndexOfSkippedRows));
          final String _tmpFileHash;
          if (_stmt.isNull(_columnIndexOfFileHash)) {
            _tmpFileHash = null;
          } else {
            _tmpFileHash = _stmt.getText(_columnIndexOfFileHash);
          }
          _item = new CsvMetadata(_tmpFileType,_tmpImportedAt,_tmpRecordCount,_tmpSkippedRows,_tmpFileHash);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Flow<List<CsvMetadata>> getAllFlow() {
    final String _sql = "SELECT * FROM csv_metadata";
    return FlowUtil.createFlow(__db, false, new String[] {"csv_metadata"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfFileType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fileType");
        final int _columnIndexOfImportedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importedAt");
        final int _columnIndexOfRecordCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "recordCount");
        final int _columnIndexOfSkippedRows = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skippedRows");
        final int _columnIndexOfFileHash = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fileHash");
        final List<CsvMetadata> _result = new ArrayList<CsvMetadata>();
        while (_stmt.step()) {
          final CsvMetadata _item;
          final String _tmpFileType;
          if (_stmt.isNull(_columnIndexOfFileType)) {
            _tmpFileType = null;
          } else {
            _tmpFileType = _stmt.getText(_columnIndexOfFileType);
          }
          final String _tmpImportedAt;
          if (_stmt.isNull(_columnIndexOfImportedAt)) {
            _tmpImportedAt = null;
          } else {
            _tmpImportedAt = _stmt.getText(_columnIndexOfImportedAt);
          }
          final int _tmpRecordCount;
          _tmpRecordCount = (int) (_stmt.getLong(_columnIndexOfRecordCount));
          final int _tmpSkippedRows;
          _tmpSkippedRows = (int) (_stmt.getLong(_columnIndexOfSkippedRows));
          final String _tmpFileHash;
          if (_stmt.isNull(_columnIndexOfFileHash)) {
            _tmpFileHash = null;
          } else {
            _tmpFileHash = _stmt.getText(_columnIndexOfFileHash);
          }
          _item = new CsvMetadata(_tmpFileType,_tmpImportedAt,_tmpRecordCount,_tmpSkippedRows,_tmpFileHash);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
