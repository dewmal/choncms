package org.chon.core.common.db;

import java.sql.ResultSet;

public interface ResultSetCallback {
	boolean process(ResultSet rs);
}
