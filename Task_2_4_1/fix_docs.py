import os
import glob
def fix_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    # Address param lines (needs empty line before)
    lines = content.split('\n')
    new_lines = []
    in_javadoc = False
    for i, line in enumerate(lines):
        stripped = line.strip()
        # summary javadocs fix
        if stripped == "/**":
            in_javadoc = True
            new_lines.append(line)
            continue
        if stripped == "*/" and in_javadoc:
            in_javadoc = False
            new_lines.append(line)
            continue
        if in_javadoc:
            # If the line starts with @param, @return, we check if the previous line was just ' *'
            if (stripped.startswith("* @param") or stripped.startswith("* @return")):
                if len(new_lines) > 0 and new_lines[-1].strip() != "*":
                    # insert empty javadoc line
                    new_lines.append(line.replace(stripped, "*"))
        new_lines.append(line)
    with open(filepath, 'w') as f:
        f.write('\n'.join(new_lines))
for root, dirs, files in os.walk('src/main/java'):
    for name in files:
        if name.endswith('.java'):
            fix_file(os.path.join(root, name))
# Also fix String Student concat missing spaces
domain_student = 'src/main/java/ru/nsu/ryzhneva/domain/Student.java'
if os.path.exists(domain_student):
    with open(domain_student, 'r') as f:
        c = f.read()
    c = c.replace("\"Student{\" +\n                \"githubUsername='\" + githubUsername + '\\'' +\n                \", fullName='\" + fullName + '\\'' +\n                \", repositoryUrl='\" + repositoryUrl + '\\'' +\n                '}';",
    "\"Student{\" \n                + \"githubUsername='\" + githubUsername + '\\'' \n                + \", fullName='\" + fullName + '\\'' \n                + \", repositoryUrl='\" + repositoryUrl + '\\'' \n                + '}';")
    # Quick fix for summary javadoc error in domain/results files
    c = c.replace("/**\n * @return логин", "/**\n * Возвращает логин.\n *\n * @return логин")
    c = c.replace("/**\n * @return полное имя", "/**\n * Возвращает имя.\n *\n * @return полное имя")
    c = c.replace("/**\n * @return полная ссылка", "/**\n * Возвращает URL.\n *\n * @return полная ссылка")
    with open(domain_student, 'w') as f:
        f.write(c)
