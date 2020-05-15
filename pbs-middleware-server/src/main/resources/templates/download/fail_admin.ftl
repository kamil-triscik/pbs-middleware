<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Download problems</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
        }

        .header {
            color: rgb(36, 140, 202);
        }

        .body {
            padding: 30px 30px 30px 30px;
            background-color: #eaeaea;
            height: 100%
        }

        .content {
            margin-bottom: 100px;
            padding: 20px;
        }

        .footer {
            padding: 15px 15px 15px 30px;
            background-color: #777777;
            font-size: 11px;
        }

        .scrollable-table {
            overflow-x: scroll;
        }

        .tg th {
            font-size: 14px;
            font-weight: normal;
            padding: 10px 5px;
            border-style: solid;
            border-width: 0px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            color: #333;
            background-color: #A8A8A8;
        }

        .failed-download-div {
            border-style: solid;
            border-width: 1px;
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 20px;
        }

        .tg .tg-cly1 {
            text-align: left;
            vertical-align: middle
        }
    </style>
</head>
<body>
<div class="body">
    <h1 align="center" class="header">
        <img src="cid:logo.png" width="45" height="30" style="margin-bottom: -5px"/>
        LL Middleware
    </h1>
    <div>
        <h2>Download problems</h2>
    </div>
    <div class="content">
        <#if count == 1>
            <p>${count} download process with problem</b>.</p><br>
        <#else>
            <p>${count} download processes with problem</b>.</p><br>
        </#if>
        <#list failures as download, failure>
            <div class="failed-download-div">
                <h4><b>Download: ${download}</b></h4>
                <table class="tg">
                    <#if description??>
                        <tr>
                            <th class="tg-cly1"><b>Description:</b></th>
                            <td>${failure['description']}</td>
                        </tr>
                    </#if>
                    <tr>
                        <th class="tg-cly1"><b>Start time:</b></th>
                        <td>${failure['start']}</td>
                    </tr>
                    <tr>
                        <th class="tg-cly1"><b>Connection:</b></th>
                        <td>${failure['connection']}</td>
                    </tr>
                    <tr>
                        <th class="tg-cly1"><b>Source:</b></th>
                        <td>${failure['source']}</td>
                    </tr>
                    <tr>
                        <th class="tg-cly1"><b>Files count:</b></th>
                        <td>${failure['count']}</td>
                    </tr>
                    <tr>
                        <th class="tg-cly1"><b>Total size:</b></th>
                        <td>${failure['size']}</td>
                    </tr>
                </table>
                <#if files?has_content>
                    <div class="scrollable-table wrapper">
                        <table class="tg" style="margin-bottom: 25px;">
                            <tr>
                                <th class="tg-cly1"><b>#</b></th>
                                <th class="tg-cly1"><b>File name:</b></th>
                                <th class="tg-cly1"><b>File size:</b></th>
                                <th class="tg-cly1"><b>Target:</b></th>
                                <th class="tg-cly1"><b>State:</b></th>
                            </tr>
                            <#list failure['files'] as index, file>
                                <tr>
                                    <td><b>${index}</b></td>
                                    <td>${file['name']}</td>
                                    <td>${file['size']}</td>
                                    <td>${file['source']}</td>
                                    <td>${file['state']}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </#if>
                <#if failure['error']?? || failure['errorMessage']??>
                    <div>
                        <h4>Reason:</h4>
                        <#if failure['errorMessage']??>
                            <p><h5><b>Message:</b></h5><br></br>${failure['errorMessage']}</p>
                        </#if>
                        <#if failure['error']??>
                            <div style="max-height: 300px; overflow:auto;">
                                <p><h5><b>Stack trace:</b></h5></p>
                                <pre>
                                    ${failure['error']}
                                </pre>
                            </div>
                        </#if>
                    </div>
                </#if>
            </div>
        </#list>
    </div>
    <div class="footer">
        <p>This message was automatically generated by Loschmidt laboratories middleware. Please do not reply
            directly recipient this email address. If you got any difficulties accessing the system or understanding
            this message, please <a href="mailto: kamil.triscik@gmail.com">system admin</a>.</p>

    </div>
</div>
</body>
</html>