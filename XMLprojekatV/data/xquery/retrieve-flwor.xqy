xquery version "3.0";
declare namespace b = "http://www.ftn.uns.ac.rs/examples/xmldb/bookstore";
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Bookstore details</title>
  </head>
  <body>
    <h1>Books available (category: web):</h1>
    <table border="1">
      <thead>
        <tr>
          <th>#</th>
          <th>Title</th>
          <th>Category</th>
          <th>Year</th>
          <th>Price</th>
        </tr>
      </thead>
      <tbody>{
        for $book at $pos in doc("2.xml")/b:bookstore/b:book[@category="WEB"]
        let $category := fn:lower-case(data($book/@category))
        order by $book/b:price ascending
        return
        <tr>
          <td>{ $pos }</td>
          <td>{ $book/b:title/text() }</td>
          <td>{ $category }</td>
          <td>{ $book/b:year/text() }</td>
          <td>{ $book/b:price/text() }</td>
        </tr>
      }</tbody>
    </table>
    <h1>Books available (category: other):</h1>
    <table border="1">
      <thead>
        <tr>
          <th>#</th>
          <th>Title</th>
          <th>Category</th>
          <th>Year</th>
          <th>Price</th>
        </tr>
      </thead>
      <tbody>{
        for $book at $pos in doc("2.xml")/b:bookstore/b:book
        let $category := fn:lower-case(data($book/@category))
        where $category != 'web'
        order by $book/b:price ascending
        return
        <tr>
          <td>{ $pos }</td>
          <td>{ $book/b:title/text() }</td>
          <td>{ $category }</td>
          <td>{ $book/b:year/text() }</td>
          <td>{ $book/b:price/text() }</td>
        </tr>
      }</tbody>
    </table>
  </body>
</html>