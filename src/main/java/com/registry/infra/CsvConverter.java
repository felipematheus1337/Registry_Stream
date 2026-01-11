package com.registry.infra;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvConverter {

    private final CsvMapper csvMapper;

    public CsvConverter() {
        this.csvMapper = new CsvMapper();
    }

    public <T> byte[] toCsvBytes(List<T> data, Class<T> type) {
        if (data == null || data.isEmpty()) {
            CsvSchema schema = schemaFor(type);
            String headerOnly = schema.getColumnDesc();
            return writeCsv(List.<T>of(), schema).getBytes(StandardCharsets.UTF_8);
        }

        CsvSchema schema = schemaFor(type);
        String csv = writeCsv(data, schema);
        return csv.getBytes(StandardCharsets.UTF_8);
    }

    private <T> CsvSchema schemaFor(Class<T> type) {
        return csvMapper
                .schemaFor(type)
                .withHeader()
                .withColumnSeparator(',');
    }

    private <T> String writeCsv(List<T> data, CsvSchema schema) {
        try {
            ObjectWriter writer = csvMapper.writer(schema);
            StringWriter out = new StringWriter();
            writer.writeValues(out).writeAll(data);
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar CSV", e);
        }
    }
}
