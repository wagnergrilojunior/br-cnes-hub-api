package br.com.cneshub.core.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import br.com.cneshub.client.CkanClient;
import br.com.cneshub.core.dto.PackageItem;
import br.com.cneshub.core.dto.PackageListResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CnesService {

    private final CkanClient client;

    public PackageListResponse listarPacotes(int page, int size, @Nullable String q) {
        List<String> packages = client.packageList().block();
        if (packages == null) {
            packages = List.of();
        }

        List<String> processed = packages.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            processed = processed.stream()
                    .filter(name -> name.toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        int total = processed.size();
        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);

        List<PackageItem> items = processed.subList(from, to).stream()
                .map(PackageItem::new)
                .collect(Collectors.toList());

        return new PackageListResponse(total, page, size, items);
    }
}

