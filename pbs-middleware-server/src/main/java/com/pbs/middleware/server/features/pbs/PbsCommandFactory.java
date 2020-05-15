package com.pbs.middleware.server.features.pbs;

import com.pbs.middleware.server.features.pbs.qstat.QstatCommandBuilder;
import com.pbs.middleware.server.features.pbs.qsub.QsubCommandBuilder;

public class PbsCommandFactory {

    public static QsubCommandBuilder qsub() {
        return new QsubCommandBuilder();
    }

    public static QstatCommandBuilder qstat() {
        return new QstatCommandBuilder();
    }

}
