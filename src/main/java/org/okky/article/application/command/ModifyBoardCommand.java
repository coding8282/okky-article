package org.okky.article.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModifyBoardCommand {
    String id;
    String name;
    String shortDescription;
    String description;
}
