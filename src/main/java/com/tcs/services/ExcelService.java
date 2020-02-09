package com.tcs.services;

import com.tcs.models.User;

import java.util.ArrayList;
import java.util.List;

public interface ExcelService {

    public List<User> getUserListFromExcel(String filename);
}
