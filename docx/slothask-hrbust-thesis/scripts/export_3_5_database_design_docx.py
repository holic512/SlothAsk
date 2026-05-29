from __future__ import annotations

import subprocess
from pathlib import Path


ROOT = Path(__file__).resolve().parents[3]
THESIS_DIR = ROOT / "docx" / "slothask-hrbust-thesis"
SOURCE_MD = THESIS_DIR / "sections" / "05-第3章系统设计-3.5数据库设计.md"
TEMP_MD = THESIS_DIR / "sections" / "05-第3章系统设计-3.5数据库设计.export.md"
REFERENCE_DOC = THESIS_DIR / "slothask-hrbust-thesis.docx"
OUTPUT_DOCX = THESIS_DIR / "slothask-hrbust-thesis-3.5-database-design.docx"


def build_export_markdown() -> None:
    text = SOURCE_MD.read_text(encoding="utf-8")
    TEMP_MD.write_text(text, encoding="utf-8")


def export_docx() -> None:
    cmd = [
        "pandoc",
        TEMP_MD.as_posix(),
        "--from=markdown+pipe_tables",
        "--to=docx",
        "--standalone",
        f"--reference-doc={REFERENCE_DOC.as_posix()}",
        f"--output={OUTPUT_DOCX.as_posix()}",
        f"--resource-path={THESIS_DIR.as_posix()}",
    ]
    subprocess.run(cmd, check=True, cwd=SOURCE_MD.parent)


def main() -> None:
    if not SOURCE_MD.exists():
        raise FileNotFoundError(f"Missing source markdown: {SOURCE_MD}")
    if not REFERENCE_DOC.exists():
        raise FileNotFoundError(f"Missing reference docx: {REFERENCE_DOC}")
    build_export_markdown()
    export_docx()
    print(OUTPUT_DOCX)


if __name__ == "__main__":
    main()
