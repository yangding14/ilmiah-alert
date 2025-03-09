package com.ilmiah.ilmiah_alert.external.ilmiah.dto;

import java.util.List;

public record GetProjectListResp(
        int draw, int recordsTotal, int recordsFiltered, List<ProjectData> data) {}
